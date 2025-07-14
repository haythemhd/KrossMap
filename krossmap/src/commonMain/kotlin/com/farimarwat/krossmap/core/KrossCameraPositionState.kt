package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import com.farimarwat.krossmap.model.KrossCoordinate

expect class KrossCameraPositionState {

    var currentCameraPosition: KrossCoordinate?
    // Methods

    suspend fun animateCamera(latitude: Double, longitude: Double, bearing: Float = 0f)

    suspend fun changeTilt(tilt: Float)
}

@Composable
expect fun  rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    tilt: Float,
    bearing: Float
):KrossCameraPositionState