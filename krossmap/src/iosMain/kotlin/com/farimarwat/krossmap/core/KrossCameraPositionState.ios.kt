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

actual class KrossCameraPositionState(
    internal var  latitude: Double,
    internal var longitude: Double
) {

    actual var currentCameraPosition by mutableStateOf<KrossCoordinate?>(null)
    private var mapView: MKMapView? = null
    private var camera: MKMapCamera? = null


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

        val camera = MKMapCamera.cameraLookingAtCenterCoordinate(
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

        val camera = MKMapCamera.cameraLookingAtCenterCoordinate(
            centerCoordinate = coordinate,
            fromDistance = distance,
            pitch = tilt.toDouble(),
            heading = bearing.toDouble()
        )

        mapView?.setCamera(camera, animated = false)
    }

    actual suspend fun animateCamera(latitude: Double, longitude: Double){

    }

    @OptIn(ExperimentalForeignApi::class)
    internal fun setMapView(map: MKMapView){
        mapView = map
        val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
        val region = MKCoordinateRegionMakeWithDistance(
            coordinate,
            300.0,
            300.0
        )
        mapView?.setRegion(region, animated = true)

    }
}

@Composable
actual fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float
): KrossCameraPositionState {
    val state =  remember { KrossCameraPositionState(
        latitude, longitude
    ) }
    return state
}

fun zoomToDistance(zoom: Float): Double {
    return 2000000 / pow(2.0, zoom.toDouble())
}
