package com.farimarwat.krossmap.core

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine

actual class KrossMapState(
    val context: Context
) {
    actual val currentLocation by mutableStateOf<KrossCoordinate?>(null)
    actual internal val markers: SnapshotStateList<KrossMarker> = mutableStateListOf()
    actual internal val polylines = mutableStateListOf<KrossPolyLine>()


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
    val context = LocalContext.current
    val locationState = remember { KrossMapState(context) }
    return locationState
}