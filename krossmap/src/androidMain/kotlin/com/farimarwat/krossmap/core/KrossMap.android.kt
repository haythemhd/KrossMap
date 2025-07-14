package com.farimarwat.krossmap.core

import android.graphics.BitmapFactory
import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
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
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState.googleCameraPositionState
                ?: rememberCameraPositionState(),
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {

            val currentTilt by remember {
                derivedStateOf {
                    cameraPositionState.googleCameraPositionState?.position?.tilt ?: 0f
                }
            }
            val currentBearing by remember {
                derivedStateOf {
                    cameraPositionState.googleCameraPositionState?.position?.bearing ?: 0f
                }
            }
            // Use the markers list directly without derivedStateOf
            mapState.markers.forEach { marker ->
                // Use a stable, unique key (assuming title is unique)
                key(marker.title) {
                    AnimatedMarker(
                        marker = marker,
                        cameraTilt = currentTilt,
                        cameraBearing = currentBearing
                    )
                }
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
private fun AnimatedMarker(marker: KrossMarker, cameraTilt: Float, cameraBearing: Float) {
    // Create MarkerState only once, keyed by marker title (unique identifier)
    val markerState = remember(marker.title) {
        MarkerState(position = LatLng(marker.coordinate.latitude, marker.coordinate.longitude))
    }

    val latAnim = remember(marker.title) { Animatable(marker.coordinate.latitude.toFloat()) }
    val lngAnim = remember(marker.title) { Animatable(marker.coordinate.longitude.toFloat()) }

    LaunchedEffect(marker.coordinate) {
        // Launch both animations concurrently
        launch {
            latAnim.animateTo(
                marker.coordinate.latitude.toFloat(),
                animationSpec = tween(durationMillis = 2000)
            )
        }
        launch {
            lngAnim.animateTo(
                marker.coordinate.longitude.toFloat(),
                animationSpec = tween(durationMillis = 2000)
            )
        }
    }

    // Update marker position as animation progresses
    LaunchedEffect(latAnim.value, lngAnim.value) {
        markerState.position = LatLng(latAnim.value.toDouble(), lngAnim.value.toDouble())
    }

    MarkerComposable(
        state = markerState,
        flat = cameraTilt > 0f,
        rotation = if(cameraTilt > 0f) cameraBearing else 0f
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
        )  {
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

