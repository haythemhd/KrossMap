package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSString
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

@OptIn(ExperimentalForeignApi::class)
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
    LaunchedEffect(mapState.markers){
        mapState.markers.forEach { item ->
            val annotation = MKPointAnnotation()
            val coordinate = CLLocationCoordinate2DMake(item.coordinate.latitude, item.coordinate.longitude)
            annotation.setCoordinate(coordinate)
            annotation.setTitle(item.title)

            mapView.addAnnotation(annotation)
        }
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