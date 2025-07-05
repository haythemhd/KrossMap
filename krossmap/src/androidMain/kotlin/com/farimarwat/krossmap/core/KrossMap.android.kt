package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState
) {

    GoogleMap(
        cameraPositionState = cameraPositionState.googleCameraPositionState ?: rememberCameraPositionState()
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
}