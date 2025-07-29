package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.farimarwat.krossmap.model.KrossCoordinate

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState

actual class KrossCameraPositionState(
     internal val googleCameraPositionState: CameraPositionState?
) {

    actual var currentCameraPosition by mutableStateOf<KrossCoordinate?>(null)
    actual suspend fun animateCamera(latitude: Double, longitude: Double, bearing: Float){
        val position = googleCameraPositionState?.position
        position?.let { p ->
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(p.zoom)
                    .bearing(bearing)
                    .tilt(p.tilt)
                    .build()
            )
            googleCameraPositionState.animate(cameraUpdate,1200)
        }
    }

    actual suspend fun changeTilt(tilt: Float) {
        val position = googleCameraPositionState?.position
        position?.let { p ->
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(position.target.latitude, position.target.longitude))
                    .zoom(p.zoom)
                    .bearing(position.bearing)
                    .tilt(tilt)
                    .build()
            )
            googleCameraPositionState.animate(cameraUpdate)
        }
    }
}

@Composable
actual fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    tilt: Float,
    bearing: Float
): KrossCameraPositionState {
    val googleCameraPositionState = rememberCameraPositionState{
        position = CameraPosition.builder()
            .target(LatLng(latitude,longitude))
            .zoom(zoom)
            .tilt(tilt)
            .build()
    }
    return remember { KrossCameraPositionState(googleCameraPositionState) }
}