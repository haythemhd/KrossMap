package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.MapKit.MKMapView

actual class KrossCameraPositionState {

    private var mapView: MKMapView? = null
    actual suspend fun animateTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float,
        tilt: Float
    ) {
    }

    actual fun moveTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float,
        tilt: Float
    ) {
    }

    internal fun setMapView(map: MKMapView){
        mapView = map
    }
}

@Composable
actual fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float
): KrossCameraPositionState {
    val state =  remember { KrossCameraPositionState() }
    return state
}