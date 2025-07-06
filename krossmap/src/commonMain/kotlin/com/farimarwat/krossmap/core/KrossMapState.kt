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

     fun addMarker(marker: KrossMarker)
     fun removeMarker(marker: KrossMarker)

     fun addPolyLine(polyLine: KrossPolyLine)
     fun removePolyLine(polyline: KrossPolyLine)

     fun requestCurrentLocation()

     fun startLocationUpdate()
     fun stopLocationUpdate()
}

@Composable
expect fun rememberKrossMapState(): KrossMapState