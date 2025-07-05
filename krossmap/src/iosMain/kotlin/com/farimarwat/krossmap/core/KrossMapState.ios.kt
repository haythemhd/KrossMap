package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine

actual class KrossMapState {
    actual val currentLocation by mutableStateOf<KrossCoordinate?>(null)
    actual val markers: SnapshotStateList<KrossMarker> = mutableStateListOf()
    actual val polylines = mutableStateListOf<KrossPolyLine>()


    actual fun addMarker(marker: KrossMarker){
        markers.add(marker)
    }
    actual fun removeMarker(marker: KrossMarker) {
        markers.remove(marker)
    }

    actual fun addPolyLine(polyLine: KrossPolyLine){
        polylines.add(polyLine)
    }

    actual fun removePolyLine(polyline: KrossPolyLine) {
        polylines.remove(polyline)
    }


}

@Composable
actual fun rememberKrossMapState(): KrossMapState {
    val locationState = remember { KrossMapState() }
    return locationState
}