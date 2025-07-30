package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine

/**
 * Maintains the state of the KrossMap, including current location, markers, polylines,
 * and location update controls.
 */
expect class KrossMapState {

     /**
      * The current location of the device, if available.
      */
     var currentLocation: KrossCoordinate?

     /**
      * List of markers currently shown on the map.
      */
     internal val markers: SnapshotStateList<KrossMarker>

     /**
      * List of polylines currently drawn on the map.
      */
     internal val polylines: SnapshotStateList<KrossPolyLine>

     /**
      * Indicates whether a location request has been made.
      */
     internal var currentLocationRequested: Boolean

     /**
      * The previous location coordinate before the current one.
      */
     internal var previousCoordinates: KrossCoordinate?



     /**
      * Callback invoked whenever the location updates.
      */
     var onUpdateLocation: (KrossCoordinate) -> Unit

     /**
      * Adds or updates a marker on the map.
      *
      * @param marker The marker to add or update.
      */
     fun addOrUpdateMarker(marker: KrossMarker)

     /**
      * Removes the given marker from the map.
      *
      * @param marker The marker to remove.
      */
     fun removeMarker(marker: KrossMarker)

     /**
      * Adds a polyline to the map.
      *
      * @param polyLine The polyline to add.
      */
     fun addPolyLine(polyLine: KrossPolyLine)

     /**
      * Removes the given polyline from the map.
      *
      * @param polyline The polyline to remove.
      */
     fun removePolyLine(polyline: KrossPolyLine)

     /**
      * Requests the current location of the user. Note: It starts and stops location updates automatically
      */
     fun requestCurrentLocation()

     /**
      * Starts continuous location updates.
      */
     fun startLocationUpdate()

     /**
      * Stops location updates.
      */
     fun stopLocationUpdate()
}

/**
 * Remembers and provides a [KrossMapState] instance for managing map interaction and state.
 */
@Composable
expect fun rememberKrossMapState(): KrossMapState
