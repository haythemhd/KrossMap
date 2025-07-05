package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker

expect class KrossMapState{
     val currentLocation: KrossCoordinate?
     val markers: SnapshotStateList<KrossMarker>
     val polylines: SnapshotStateList<List<KrossCoordinate>>

     fun addMarker(marker: KrossMarker)
     fun removeMarker(marker: KrossMarker)
     fun addPolyLine(list:List<KrossCoordinate>)
}

@Composable
expect fun rememberKrossMapState(): KrossMapState