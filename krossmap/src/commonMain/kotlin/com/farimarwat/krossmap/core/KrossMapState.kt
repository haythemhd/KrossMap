package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossLocation

expect class KrossMapState{
     val currentLocation: KrossLocation?
     val markers: SnapshotStateList<KrossLocation>
     val polylines: SnapshotStateList<List<KrossLocation>>

     fun setCamera(location: KrossLocation)
     fun addMarker(location: KrossLocation)
     fun addPolyLine(list:List<KrossLocation>)
}

@Composable
expect fun rememberKrossMapState(): KrossMapState