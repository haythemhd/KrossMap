package com.farimarwat.krossmap.core

import android.graphics.BitmapFactory
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.farimarwat.krossmap.model.KrossMarker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
actual fun KrossMap(
    modifier: Modifier,
    mapState: KrossMapState,
    cameraPositionState: KrossCameraPositionState,
    mapSettings: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState.googleCameraPositionState
                ?: rememberCameraPositionState(),
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {

            LaunchedEffect(cameraPositionState.tilt) {
                cameraPositionState.animateCamera(tilt = cameraPositionState.tilt)
            }

            val currentBearing by remember {
                derivedStateOf {
                    mapState.currentLocation?.bearing ?: 0f
                }
            }
            LaunchedEffect(currentBearing) {
                scope.launch {
                    mapState.currentLocation?.let{
                        if (cameraPositionState.cameraFollow) {
                            cameraPositionState.animateCamera(
                                latitude = it.latitude,
                                longitude = it.longitude,
                                bearing = currentBearing,
                            )
                        }
                    }
                }
            }
            // Use the markers list directly without derivedStateOf
            mapState.markers.forEach { marker ->
                AnimatedMarker(
                    marker = marker,
                    cameraTilt = cameraPositionState.tilt,
                    bearing = if(cameraPositionState.cameraFollow) 0f else currentBearing
                )
            }

            // Similar optimization for polylines
            mapState.polylines.forEach { polyLine ->
                key(polyLine.hashCode()) { // or use a unique ID if available
                    Polyline(
                        points = polyLine.points.map { LatLng(it.latitude, it.longitude) },
                        width = polyLine.width,
                        color = polyLine.color
                    )
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            mapSettings()
        }
    }
}

@Composable
private fun AnimatedMarker(marker: KrossMarker, cameraTilt: Float, bearing: Float) {
    println("Marker Animating: ${marker.title}")

    val targetPosition = LatLng(marker.coordinate.latitude, marker.coordinate.longitude)
    var currentPosition by remember { mutableStateOf(targetPosition) }
    var animStartPosition by remember { mutableStateOf(targetPosition) }

    val animationDuration = 1000

    LaunchedEffect(targetPosition) {
        if (targetPosition != currentPosition) {
            animStartPosition = currentPosition

            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(animationDuration, easing = LinearEasing)
            ) { value, _ ->
                currentPosition = LatLng(
                    animStartPosition.latitude + (targetPosition.latitude - animStartPosition.latitude) * value.toDouble(),
                    animStartPosition.longitude + (targetPosition.longitude - animStartPosition.longitude) * value.toDouble()
                )
            }
        }
    }

    // Always show the marker at currentPosition (the interpolated position)
    MarkerComposable(
        state = remember(currentPosition) { MarkerState(position = currentPosition) },
        flat = cameraTilt > 0f,
        rotation = bearing
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.wrapContentSize()
        ) {
            // Icon
            marker.icon?.let { data ->
                val bitmap = remember(data) {
                    BitmapFactory.decodeByteArray(data, 0, data.size)?.asImageBitmap()
                }

                bitmap?.let { imageBitmap ->
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = marker.title,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Title
            if (marker.title.isNotEmpty()) {
                Text(
                    text = marker.title,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.8f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

