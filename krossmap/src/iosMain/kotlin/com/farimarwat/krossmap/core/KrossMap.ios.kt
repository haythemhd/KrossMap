package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValue
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSString
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.addOverlay
import platform.UIKit.UIColor

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState
) {

    val mapDelegate = remember { MapViewDelegate(mapState) }
    val mapView = remember {
        MKMapView()
            .apply {
                delegate = mapDelegate
            }
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
    LaunchedEffect(mapState.polylines) {
        mapState.polylines.forEach { poly ->
            val coordinates = poly.points.map {
                CLLocationCoordinate2DMake(it.latitude, it.longitude)
            }

            // Use memScoped to allocate C array
            memScoped {
                val coordinatesArray = allocArray<CLLocationCoordinate2D>(coordinates.size)
                coordinates.forEachIndexed { index, coord ->
                    coordinatesArray.get(index).latitude = coord.useContents { latitude}
                    coordinatesArray.get(index).longitude = coord.useContents { longitude}
                }

                // Create MKPolyline from coordinates
                val polyline = MKPolyline.polylineWithCoordinates(
                    coords = coordinatesArray,
                    count = coordinates.size.toULong(),
                )
                polyline.setTitle(poly.title)

                // Add polyline to map
                mapView.addOverlay(polyline)
            }
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
fun Color.toUIColor(): UIColor {
    val red = this.red.toDouble()
    val green = this.green.toDouble()
    val blue = this.blue.toDouble()
    val alpha = this.alpha.toDouble()

    return UIColor.colorWithRed(red, green, blue, alpha)
}