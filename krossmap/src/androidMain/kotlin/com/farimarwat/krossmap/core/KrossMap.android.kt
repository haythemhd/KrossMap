package com.farimarwat.krossmap.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState,
    mapSettings:@Composable ()->Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        GoogleMap(
            cameraPositionState = cameraPositionState.googleCameraPositionState ?: rememberCameraPositionState(),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false
            )
        ) {
            mapState.markers.forEach { item ->
                Marker(
                    state = MarkerState(LatLng(item.coordinate.latitude,item.coordinate.longitude)),
                    title = item.title
                )
            }

            mapState.polylines.forEach { poly ->
                Polyline(
                    points = poly.points.map { LatLng(it.latitude, it.longitude) },
                    color = poly.color,
                    width = poly.width
                )
            }
        }
       Box(
           modifier = Modifier.align(Alignment.BottomEnd)
       ){
           mapSettings()
       }
    }
}