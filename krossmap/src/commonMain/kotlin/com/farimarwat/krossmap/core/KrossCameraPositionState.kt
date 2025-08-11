package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import com.farimarwat.krossmap.model.KrossCoordinate

/**
 * A multiplatform representation of a map camera controller and its state.
 *
 * This class provides camera properties like tilt and follow behavior,
 * and exposes a method to animate the camera to a new position with optional
 * bearing, tilt, and zoom parameters.
 *
 * This is an `expect` class and should have platform-specific implementations.
 */
expect class KrossCameraPositionState {

    /**
     * The tilt angle of the camera, in degrees.
     * A tilt of 0 is directly overhead, while higher values represent angled views.
     */
    var tilt: Float

    /**
     * Whether the camera should follow the user's or marker's movement automatically.
     * When true, user interactions like dragging the map may reset camera position.
     */
    var cameraFollow: Boolean

    /**
     * Current center of the visible map region (camera target), if available.
     * Returns null if the underlying map view/state is not yet ready.
     */
    val center: KrossCoordinate?

    /**
     * Animates the camera to the specified location and orientation.
     *
     * @param latitude The target latitude to move to. If null, the latitude remains unchanged.
     * @param longitude The target longitude to move to. If null, the longitude remains unchanged.
     * @param bearing The new direction (in degrees) the camera should face. If null, bearing is unchanged.
     * @param tilt The desired tilt angle for the camera. If null, tilt remains unchanged.
     * @param zoom The zoom level to apply. If null, the current zoom remains unchanged.
     * @param durationMillis The duration of the animation in milliseconds. Default is 1000ms.
     */
    suspend fun animateCamera(
        latitude: Double? = null,
        longitude: Double? = null,
        bearing: Float? = null,
        tilt: Float? = null,
        zoom: Float? = null,
        durationMillis: Int = 1000
    )
}


/**
 * Creates and remembers a [KrossCameraPositionState] to manage camera movement and tracking
 * within a Compose environment.
 *
 * This state object persists across recompositions and can be used to programmatically
 * control the map camera (e.g., animate or track movements).
 *
 * @param latitude The initial latitude for the camera.
 * @param longitude The initial longitude for the camera.
 * @param zoom The initial zoom level. Default is 0f.
 * @param tilt The initial tilt of the camera in degrees. Default is 0f.
 * @param bearing The initial bearing or orientation the camera should face. Default is 0f.
 * @param cameraFollow Whether the camera should automatically follow movement. Default is true.
 * @return A remembered [KrossCameraPositionState] instance tied to the composition lifecycle.
 */
@Composable
expect fun rememberKrossCameraPositionState(
    latitude: Double,
    longitude: Double,
    zoom: Float = 0f,
    tilt: Float = 0f,
    bearing: Float = 0f,
    cameraFollow: Boolean = true
): KrossCameraPositionState

