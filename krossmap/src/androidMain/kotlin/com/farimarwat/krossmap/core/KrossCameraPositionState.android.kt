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
    actual suspend fun animateTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float,
        tilt: Float
    ) {
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(zoom)
                .bearing(bearing)
                .tilt(tilt)
                .build()
        )
        googleCameraPositionState?.animate(cameraUpdate)
    }

    actual fun moveTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float,
        tilt: Float
    ) {
        googleCameraPositionState?.move(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    LatLng(latitude, longitude),
                    zoom,
                    tilt,
                    bearing
                )
            )
        )
    }
    actual suspend fun animateCamera(latitude: Double, longitude: Double){
        val position = googleCameraPositionState?.position
        position?.let { p ->
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(p.zoom)
                    .bearing(p.bearing)
                    .tilt(p.tilt)
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
    tilt: Float
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