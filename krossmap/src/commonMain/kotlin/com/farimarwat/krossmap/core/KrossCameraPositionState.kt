package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable

expect class KrossCameraPositionState {

    // Methods
    suspend fun animateTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float = 0f,
        tilt: Float = 0f
    )

    fun moveTo(
        latitude: Double,
        longitude: Double,
        zoom: Float,
        bearing: Float = 0f,
        tilt: Float = 0f
    )
}

@Composable
expect fun  rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float
):KrossCameraPositionState