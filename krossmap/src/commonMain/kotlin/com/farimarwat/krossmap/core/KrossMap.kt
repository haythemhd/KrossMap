package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A platform-agnostic Composable function that renders a map view.
 *
 * This Composable is expected to be implemented per platform (e.g., Android/iOS) using Kotlin Multiplatform.
 *
 * @param modifier The [Modifier] to be applied to the map layout.
 * @param mapState The [KrossMapState] state holder managing map interactions like taps, long presses, polylines, etc.
 * @param cameraPositionState [KrossCameraPositionState] Controls and observes the current camera (viewport) position of the map.
 * @param properties Defines visual and gesture-related settings via [KrossMapProperties]. Defaults to [KrossMapPropertiesDefaults.defaults].
 * @param mapSettings A composable lambda placed at the bottom-right corner of the map, useful for showing buttons or overlays.
 */
@Composable
expect fun KrossMap(
    modifier: Modifier = Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState,
    properties: KrossMapProperties = KrossMapPropertiesDefaults.defaults(),
    mapSettings: @Composable () -> Unit = {}
)
