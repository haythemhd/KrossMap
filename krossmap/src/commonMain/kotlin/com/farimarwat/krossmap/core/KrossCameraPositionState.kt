package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable

/**
 * Represents the camera state for the map, including the current position and camera controls.
 */
expect class KrossCameraPositionState {


    var tilt: Float

    /**
     * Animates the camera to the given latitude and longitude with an optional bearing.
     *
     * @param latitude Target latitude.
     * @param longitude Target longitude.
     * @param bearing Direction the camera should face, in degrees. Default is 0.
     */
    suspend fun animateCamera(
        latitude: Double? = null,
        longitude: Double? = null,
        bearing: Float? = null,
        tilt:Float? = null,
        zoom:Float? = null,
        durationMillis: Int = 1000
        )
}

/**
 * Remembers and provides a [KrossCameraPositionState] for controlling and tracking the map camera.
 *
 * @param latitude Initial latitude position.
 * @param longitude Initial longitude position.
 * @param zoom Initial zoom level.
 * @param tilt Initial cameraTilt angle of the camera.
 * @param bearing Initial bearing (direction) the camera should face.
 */
@Composable
expect fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    tilt: Float,
    bearing: Float
): KrossCameraPositionState
