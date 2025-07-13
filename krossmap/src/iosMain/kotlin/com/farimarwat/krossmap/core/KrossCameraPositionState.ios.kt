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
        val distance = zoomToDistance(zoom)

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
        val distance = zoomToDistance(zoom)

        camera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = distance,
            pitch = tilt.toDouble(),
            heading = bearing.toDouble()
        )

        mapView?.setCamera(camera, animated = false)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun animateCamera(latitude: Double, longitude: Double, bearing: Float) {
        val coordinate = CLLocationCoordinate2DMake(latitude, longitude)

        // Get current camera to preserve zoom, tilt, and bearing
        val currentCamera = mapView?.camera

        val newCamera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = currentCamera?.altitude ?: 10000.0,
            pitch = currentCamera?.pitch ?: 0.0,
            heading = bearing.toDouble()
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
        val distance = zoomToDistance(zoom)

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
fun zoomToDistance(zoom: Float): Double {
    val baseDistance =  when {
        zoom >= 20 -> 200.0       // was 170.0
        zoom >= 19 -> 400.0       // was 340.0
        zoom >= 18 -> 600.0       // was 500.0
        zoom >= 17 -> 1200.0      // was 1000.0
        zoom >= 16 -> 2400.0      // was 2000.0
        zoom >= 15 -> 4800.0      // was 4000.0
        zoom >= 14 -> 9600.0      // was 8000.0
        zoom >= 13 -> 19200.0     // was 16000.0
        zoom >= 12 -> 38400.0     // was 32000.0
        zoom >= 11 -> 76800.0     // was 64000.0
        zoom >= 10 -> 153600.0    // was 128000.0
        zoom >= 9 -> 307200.0     // was 256000.0
        zoom >= 8 -> 614400.0     // was 512000.0
        zoom >= 7 -> 1228800.0    // was 1024000.0
        zoom >= 6 -> 2457600.0    // was 2048000.0
        zoom >= 5 -> 4915200.0    // was 4096000.0
        zoom >= 4 -> 9830400.0    // was 8192000.0
        zoom >= 3 -> 19660800.0   // was 16384000.0
        zoom >= 2 -> 39321600.0   // was 32768000.0
        zoom >= 1 -> 78643200.0   // was 65536000.0
        else -> 157286400.0       // was 131072000.0
    }
    return baseDistance * 1.5
}

