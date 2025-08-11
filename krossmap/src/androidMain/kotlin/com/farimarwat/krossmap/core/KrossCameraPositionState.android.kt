package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.farimarwat.krossmap.model.KrossCoordinate

actual class KrossCameraPositionState(
     internal val googleCameraPositionState: CameraPositionState?
) {

    actual var tilt by mutableStateOf(0f)
    actual var cameraFollow by mutableStateOf(true)

    actual val center: KrossCoordinate?
        get() {
            val position = googleCameraPositionState?.position ?: return null
            val target = position.target
            return KrossCoordinate(
                latitude = target.latitude,
                longitude = target.longitude,
                bearing = position.bearing
            )
        }
    actual suspend fun animateCamera(
        latitude: Double?,
        longitude: Double?,
        bearing: Float?,
        tilt:Float?,
        zoom:Float?,
        durationMillis: Int){
        val position = googleCameraPositionState?.position
        position?.let { p ->
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(latitude ?: p.target.latitude, longitude ?: p.target.longitude))
                    .zoom(zoom ?: p.zoom)
                    .bearing(bearing ?: p.bearing)
                    .tilt(tilt ?: p.tilt)
                    .build()
            )
            googleCameraPositionState.animate(cameraUpdate,1200)
        }
    }


}

@Composable
actual fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    tilt: Float,
    bearing: Float,
    cameraFollow: Boolean
): KrossCameraPositionState {
    val googleCameraPositionState = rememberCameraPositionState{
        position = CameraPosition.builder()
            .target(LatLng(latitude,longitude))
            .zoom(zoom)
            .tilt(tilt)
            .build()
    }
    return remember {
        KrossCameraPositionState(googleCameraPositionState).apply {
            this.cameraFollow = cameraFollow
        }
    }
}