package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.farimarwat.krossmap.model.KrossCoordinate
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapCamera
import platform.MapKit.MKMapView
import platform.posix.pow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2

actual class KrossCameraPositionState(
    private var camera: MKMapCamera
) {

    actual var currentCameraPosition by mutableStateOf<KrossCoordinate?>(null)
    private var mapView: MKMapView? = null



    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun animateTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float,
        tilt: Float
    ) {
        val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
        val distance = zoomToDistance(zoom, latitude)

        camera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = distance,
            pitch = tilt.toDouble(),
            heading = bearing.toDouble()
        )

        mapView?.setCamera(camera, animated = true)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun moveTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float,
        tilt: Float
    ) {
        val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
        val distance = zoomToDistance(zoom, latitude)

        camera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = distance,
            pitch = tilt.toDouble(),
            heading = bearing.toDouble()
        )

        mapView?.setCamera(camera, animated = false)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun animateCamera(latitude: Double, longitude: Double) {
        val coordinate = CLLocationCoordinate2DMake(latitude, longitude)

        // Get current camera to preserve zoom, tilt, and bearing
        val currentCamera = mapView?.camera

        val newCamera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = currentCamera?.altitude ?: 10000.0,
            pitch = currentCamera?.pitch ?: 0.0,
            heading = currentCamera?.heading ?: 0.0
        )

        mapView?.setCamera(newCamera, animated = true)
    }

    @OptIn(ExperimentalForeignApi::class)
    internal fun setMapView(map: MKMapView) {
        mapView = map
        mapView?.setCamera(camera)

    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    tilt: Float,
    bearing: Float
): KrossCameraPositionState {
    val state = remember {
        val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
        val distance = zoomToDistance(zoom, latitude)

        val camera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = distance,
            pitch = tilt.toDouble(),
            heading = bearing.toDouble()
        )
        KrossCameraPositionState(
            camera
        )
    }
    return state
}
fun zoomToDistance(zoom: Float, latitude: Double): Double {
    return when {
        zoom >= 20 -> 170.0     // Tested and working value
        zoom >= 19 -> 340.0     // 2x larger distance
        zoom >= 18 -> 500.0     // Reduced from 680 - roads were too small, so camera closer
        zoom >= 17 -> 1000.0    // ~2x from 18
        zoom >= 16 -> 2000.0    // ~2x from 17
        zoom >= 15 -> 4000.0    // ~2x from 16
        zoom >= 14 -> 8000.0    // ~2x from 15
        zoom >= 13 -> 16000.0   // ~2x from 14
        zoom >= 12 -> 32000.0   // ~2x from 13
        zoom >= 11 -> 64000.0   // ~2x from 12
        zoom >= 10 -> 128000.0  // ~2x from 11
        zoom >= 9 -> 256000.0   // ~2x from 10
        zoom >= 8 -> 512000.0   // ~2x from 9
        zoom >= 7 -> 1024000.0  // ~2x from 8
        zoom >= 6 -> 2048000.0  // ~2x from 7
        zoom >= 5 -> 4096000.0  // ~2x from 6
        zoom >= 4 -> 8192000.0  // ~2x from 5
        zoom >= 3 -> 16384000.0 // ~2x from 4
        zoom >= 2 -> 32768000.0 // ~2x from 3
        zoom >= 1 -> 65536000.0 // ~2x from 2
        else -> 131072000.0     // ~2x from 1
    }
}
