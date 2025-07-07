package com.farimarwat.krossmap.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValue
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSString
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
    mapSettings:@Composable ()->Unit
) {
    val initialMarkers by remember {
        derivedStateOf { mapState.markers.toList() }
    }
    mapState.setCameraPositionState(cameraPositionState)
    mapState.setCameraPositionState(cameraPositionState)
    LaunchedEffect(cameraPositionState.currentCameraPosition){
        println("MyPosition: ${"Working"}")

        cameraPositionState.currentCameraPosition?.let{position ->
            println("MyPosition: ${position}")
            val latitude = position.latitude
            val longitude = position.longitude
            cameraPositionState.animateCamera(
                latitude,
                longitude
            )
        }
    }
    val mapDelegate = remember { MapViewDelegate(mapState) }
    val mapView = remember {
        MKMapView()
            .apply {
                delegate = mapDelegate
            }
    }
    LaunchedEffect(Unit){
        cameraPositionState.setMapView(mapView)
    }
    // In your iOS KrossMap composable
    LaunchedEffect(initialMarkers) {
        initialMarkers.forEach { item ->
            val currentAnnotations = mapView.annotations.filterIsInstance<MKPointAnnotation>()
            val existingAnnotation = currentAnnotations.find { annotation ->
                annotation.title == item.title
            }

            if (existingAnnotation != null) {
                // Update existing annotation coordinate
                val coordinate = CLLocationCoordinate2DMake(item.coordinate.latitude, item.coordinate.longitude)
                existingAnnotation.setCoordinate(coordinate)
            } else {
                // Add new annotation
                val annotation = MKPointAnnotation()
                val coordinate = CLLocationCoordinate2DMake(item.coordinate.latitude, item.coordinate.longitude)
                annotation.setCoordinate(coordinate)
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

            // Use memScoped to allocate C array
            memScoped {
                val coordinatesArray = allocArray<CLLocationCoordinate2D>(coordinates.size)
                coordinates.forEachIndexed { index, coord ->
                    coordinatesArray.get(index).latitude = coord.useContents { latitude}
                    coordinatesArray.get(index).longitude = coord.useContents { longitude}
                }

                // Create MKPolyline from coordinates
                val polyline = MKPolyline.polylineWithCoordinates(
                    coords = coordinatesArray,
                    count = coordinates.size.toULong(),
                )
                polyline.setTitle(poly.title)

                // Add polyline to map
                mapView.addOverlay(polyline)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        UIKitView(
            factory = {

                mapView
            },
            update = {map ->

            },
            modifier = modifier
        )
        Box(
            modifier = Modifier.align(Alignment.BottomEnd)
        ){
            mapSettings()
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