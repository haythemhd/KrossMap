package com.farimarwat.krossmap.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val initialMarkers by remember {
        derivedStateOf { mapState.markers.toList() }
    }
    val initialPolyLine by remember {
        derivedStateOf { mapState.polylines.toList() }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        mapState.setCameraPositionState(cameraPositionState)
        LaunchedEffect(cameraPositionState.currentCameraPosition){
            println("MyPosition: ${"Working"}")

            cameraPositionState.currentCameraPosition?.let{position ->
                println("MyPosition: ${position}")
                val latitude = position.latitude
                val longitude = position.longitude
                cameraPositionState.animateCamera(
                    latitude,
                    longitude
                )
            }
        }
        GoogleMap(
            cameraPositionState = cameraPositionState.googleCameraPositionState ?: rememberCameraPositionState(),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false
            )
        ) {
            println("Google Map Updated")
            initialMarkers.forEach { item ->
                val markerState = remember { MarkerState() }
                // This is the key - update the existing MarkerState
                LaunchedEffect(item.coordinate) {
                    markerState.position = LatLng(item.coordinate.latitude, item.coordinate.longitude)
                }

                Marker(
                    state = markerState,
                    title = item.title
                )
            }

            initialPolyLine.forEach { polyLine ->
                Polyline(
                    points = polyLine.points.map { LatLng(it.latitude,it.longitude) },
                    width = polyLine.width,
                    color = polyLine.color
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