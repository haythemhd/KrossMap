package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.farimarwat.krossmap.model.KrossCoordinate
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapCamera
import platform.MapKit.MKMapView
import platform.posix.pow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log2
import kotlin.math.pow

actual class KrossCameraPositionState(
    private var camera: MKMapCamera
) {

    actual var tilt by mutableStateOf(0f)
    private var mapView: MKMapView? = null
    actual var cameraFollow by mutableStateOf(false)

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun animateCamera(
        latitude: Double?,
        longitude: Double?,
        bearing: Float?,
        tilt: Float?,
        zoom: Float?,
        durationMillis: Int
    ) {
        val currentCamera = mapView?.camera ?: return
        val currentCoordinate = currentCamera.centerCoordinate ?: return
        val (lat, lng) = currentCoordinate.useContents { this.latitude to this.longitude }

        val coordinate = CLLocationCoordinate2DMake(latitude ?: lat, longitude ?: lng)

        val distance = zoom?.let { zoomToDistance(it) } ?: currentCamera.centerCoordinateDistance

        val newCamera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = distance,
            pitch = tilt?.toDouble() ?: currentCamera.pitch,
            heading = bearing?.toDouble() ?: currentCamera.heading
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
    bearing: Float,
    cameraFollow: Boolean
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
        ).apply {
            this.cameraFollow = cameraFollow
        }
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
    return baseDistance * 1.2
}

