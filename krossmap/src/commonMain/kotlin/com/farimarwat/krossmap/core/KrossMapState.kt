package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine

/**
 * Maintains the interactive state of the KrossMap, including current user location,
 * marker and polyline management, and location update control.
 *
 * This is an `expect` class and must be implemented per platform.
 */
expect class KrossMapState {

     /**
      * The current location of the user/device, if available.
      */
     var currentLocation: KrossCoordinate?

     /**
      * The list of markers currently displayed on the map.
      */
     internal val markers: SnapshotStateList<KrossMarker>

     /**
      * The list of polylines currently drawn on the map.
      */
     internal val polylines: SnapshotStateList<KrossPolyLine>

     /**
      * Indicates whether a current location request has been initiated.
      */
     internal var currentLocationRequested: Boolean

     /**
      * The previous location coordinate before the current one.
      */
     internal var previousCoordinates: KrossCoordinate?

     /**
      * A callback function that is triggered when the user location is updated.
      *
      * @param KrossCoordinate The new location.
      */
     var onUpdateLocation: (KrossCoordinate) -> Unit

     /**
      * Adds a new marker to the map or updates an existing one if it already exists.
      *
      * @param marker The marker to add or update.
      */
     fun addOrUpdateMarker(marker: KrossMarker)

     /**
      * Removes a specified marker from the map.
      *
      * @param marker The marker to remove.
      */
     fun removeMarker(marker: KrossMarker)

     /**
      * Adds a new polyline to the map.
      *
      * @param polyLine The polyline to add.
      */
     fun addPolyLine(polyLine: KrossPolyLine)

     /**
      * Removes a specified polyline from the map.
      *
      * @param polyline The polyline to remove.
      */
     fun removePolyLine(polyline: KrossPolyLine)

     /**
      * Requests the device's current location once.
      *
      * This method will internally start and stop location updates automatically.
      */
     fun requestCurrentLocation()

     /**
      * Begins continuous location updates.
      */
     fun startLocationUpdate()

     /**
      * Stops ongoing location updates.
      */
     fun stopLocationUpdate()
}

/**
 * Composable function that remembers and provides a [KrossMapState] instance
 * across recompositions. This state object is responsible for managing
 * the map's interactive behaviors such as markers, polylines, and location updates.
 *
 * @return A retained instance of [KrossMapState] for map control and interaction.
 */
@Composable
expect fun rememberKrossMapState(): KrossMapState
