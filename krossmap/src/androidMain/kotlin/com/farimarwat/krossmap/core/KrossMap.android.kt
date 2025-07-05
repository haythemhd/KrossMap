package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState
) {

    GoogleMap(
        cameraPositionState = cameraPositionState.googleCameraPositionState ?: rememberCameraPositionState()
    ) { }
}