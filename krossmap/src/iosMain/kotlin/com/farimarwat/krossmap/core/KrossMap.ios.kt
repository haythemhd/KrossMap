package com.farimarwat.krossmap.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.useContents
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.addOverlay
import platform.UIKit.UIColor

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState,
    properties: KrossMapProperties,
    mapSettings: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val initialMarkers by remember {
        derivedStateOf { mapState.markers.toList() }
    }

    val mapDelegate = remember { MapViewDelegate(mapState, cameraPositionState) }
    val animationHelper = remember { MarkerAnimationHelper() }

    val mapView = remember {
        MKMapView().apply {
            delegate = mapDelegate
            showsTraffic = properties.showTraffic
            showsBuildings = properties.showBuildings
            showsPointsOfInterest = properties.showPointOfInterest
            showsCompass = properties.showCompass
            rotateEnabled = properties.enableRotationGesture
            pitchEnabled = properties.enableTiltGesture
            scrollEnabled = properties.enableScrollGesture
        }
    }

    LaunchedEffect(Unit) {
        cameraPositionState.setMapView(mapView)
        mapDelegate.setMapView(mapView)
    }
    LaunchedEffect(mapState.currentLocation?.bearing, cameraPositionState.tilt) {
        mapDelegate.updateMarkerTransforms()
    }

    LaunchedEffect(cameraPositionState.tilt){
        println("Tilt changed: ${cameraPositionState.tilt}")
        cameraPositionState.animateCamera(tilt = cameraPositionState.tilt)
    }

    val currentBearing by remember {
        derivedStateOf {
            mapState.currentLocation?.bearing ?: 0f
        }
    }
    LaunchedEffect(currentBearing) {
        scope.launch {
            mapState.currentLocation?.let{
                if (cameraPositionState.cameraFollow) {
                    cameraPositionState.animateCamera(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        bearing = it.bearing,
                    )
                }
            }
        }
    }


    // Animated marker updates
    LaunchedEffect(initialMarkers) {
        initialMarkers.forEach { item ->
            val currentAnnotations = mapView.annotations.filterIsInstance<MKPointAnnotation>()
            val existingAnnotation = currentAnnotations.find { annotation ->
                annotation.title() == item.title
            }

            val targetCoordinate = CLLocationCoordinate2DMake(
                item.coordinate.latitude,
                item.coordinate.longitude
            )

            if (existingAnnotation != null) {
                // Animate existing annotation to new position
                animationHelper.animateMarker(
                    annotation = existingAnnotation,
                    toCoordinate = targetCoordinate,
                    duration = 1000L // 1 second in milliseconds
                )
            } else {
                // Add new annotation (no animation needed for new markers)
                val annotation = MKPointAnnotation()
                annotation.setCoordinate(targetCoordinate)
                annotation.setTitle(item.title)
                mapView.addAnnotation(annotation)
            }
        }
    }

    LaunchedEffect(mapState.polylines) {
        mapState.polylines.forEach { poly ->
            val coordinates = poly.points.map {
                CLLocationCoordinate2DMake(it.latitude, it.longitude)
            }

            memScoped {
                val coordinatesArray = allocArray<CLLocationCoordinate2D>(coordinates.size)
                coordinates.forEachIndexed { index, coord ->
                    coordinatesArray.get(index).latitude = coord.useContents { latitude }
                    coordinatesArray.get(index).longitude = coord.useContents { longitude }
                }

                val polyline = MKPolyline.polylineWithCoordinates(
                    coords = coordinatesArray,
                    count = coordinates.size.toULong(),
                )
                polyline.setTitle(poly.title)
                mapView.addOverlay(polyline)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        UIKitView(
            factory = { mapView },
            update = { map -> },
            modifier = modifier
        )
        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            mapSettings()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
class MarkerAnimationHelper {
    private val activeAnimations = mutableSetOf<String>()

    suspend fun animateMarker(
        annotation: MKPointAnnotation,
        toCoordinate: CValue<CLLocationCoordinate2D>,
        duration: Long
    ) {
        val annotationId = annotation.title() ?: "unknown"

        // Prevent multiple animations for the same marker
        if (activeAnimations.contains(annotationId)) {
            return
        }

        activeAnimations.add(annotationId)

        try {
            val fromCoordinate = annotation.coordinate()

            // Animation parameters
            val frameCount = 60 // 60 frames for 1 second at 60fps
            val frameDuration = duration / frameCount

            repeat(frameCount) { frame ->
                val progress = (frame + 1).toFloat() / frameCount
                val easedProgress = easeInOut(progress)

                val interpolatedCoordinate = interpolateCoordinates(
                    fromCoordinate,
                    toCoordinate,
                    easedProgress.toDouble()
                )

                annotation.setCoordinate(interpolatedCoordinate)

                if (frame < frameCount - 1) {
                    delay(frameDuration)
                }
            }

            // Ensure final position is exact
            annotation.setCoordinate(toCoordinate)

        } finally {
            activeAnimations.remove(annotationId)
        }
    }

    private fun easeInOut(t: Float): Float {
        return if (t < 0.5f) {
            2f * t * t
        } else {
            -1f + (4f - 2f * t) * t
        }
    }

    private fun interpolateCoordinates(
        from: CValue<CLLocationCoordinate2D>,
        to: CValue<CLLocationCoordinate2D>,
        progress: Double
    ): CValue<CLLocationCoordinate2D> {
        return from.useContents {
            val fromLat = latitude
            val fromLng = longitude

            to.useContents {
                val toLat = latitude
                val toLng = longitude

                val interpolatedLat = fromLat + (toLat - fromLat) * progress
                val interpolatedLng = fromLng + (toLng - fromLng) * progress

                CLLocationCoordinate2DMake(interpolatedLat, interpolatedLng)
            }
        }
    }
}

fun Color.toUIColor(): UIColor {
    val red = this.red.toDouble()
    val green = this.green.toDouble()
    val blue = this.blue.toDouble()
    val alpha = this.alpha.toDouble()

    return UIColor.colorWithRed(red, green, blue, alpha)
}