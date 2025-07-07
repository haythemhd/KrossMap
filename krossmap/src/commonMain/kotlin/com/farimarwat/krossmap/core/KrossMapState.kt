package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine

expect class KrossMapState{
     var currentLocation: KrossCoordinate?
     internal val markers: SnapshotStateList<KrossMarker>
     internal val polylines: SnapshotStateList<KrossPolyLine>

     internal var currentLocationRequested: Boolean
     internal var krossMapCameraPositionState: KrossCameraPositionState?

     fun addMarker(marker: KrossMarker)
     fun removeMarker(marker: KrossMarker)

     fun addPolyLine(polyLine: KrossPolyLine)
     fun removePolyLine(polyline: KrossPolyLine)

     fun requestCurrentLocation()

     fun startLocationUpdate()
     fun stopLocationUpdate()

     internal fun setCameraPositionState(state: KrossCameraPositionState)
}

@Composable
expect fun rememberKrossMapState(): KrossMapState