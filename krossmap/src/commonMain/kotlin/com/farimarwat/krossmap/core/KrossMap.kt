package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun KrossMap(
    modifier: Modifier = Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState,
    mapSettings:@Composable ()->Unit = {}
)