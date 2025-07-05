package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.MapKit.MKMapView

@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState
) {

    val mapView = remember {
        MKMapView()
    }
    LaunchedEffect(Unit){
        cameraPositionState.setMapView(mapView)
    }

    UIKitView(
        factory = {

            mapView
        },
        update = {map ->

        },
        modifier = modifier
    )
}