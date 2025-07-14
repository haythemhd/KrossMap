package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import com.farimarwat.krossmap.model.KrossCoordinate

/**
 * Represents the camera state for the map, including the current position and camera controls.
 */
expect class KrossCameraPositionState {

    /**
     * The current position of the camera on the map.
     */
    var currentCameraPosition: KrossCoordinate?

    /**
     * Animates the camera to the given latitude and longitude with an optional bearing.
     *
     * @param latitude Target latitude.
     * @param longitude Target longitude.
     * @param bearing Direction the camera should face, in degrees. Default is 0.
     */
    suspend fun animateCamera(latitude: Double, longitude: Double, bearing: Float = 0f)

    /**
     * Changes the tilt angle of the camera.
     *
     * @param tilt The new tilt angle in degrees.
     */
    suspend fun changeTilt(tilt: Float)
}

/**
 * Remembers and provides a [KrossCameraPositionState] for controlling and tracking the map camera.
 *
 * @param latitude Initial latitude position.
 * @param longitude Initial longitude position.
 * @param zoom Initial zoom level.
 * @param tilt Initial tilt angle of the camera.
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
