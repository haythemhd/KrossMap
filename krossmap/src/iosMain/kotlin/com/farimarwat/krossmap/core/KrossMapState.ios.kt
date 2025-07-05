package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.farimarwat.krossmap.model.KrossLocation

actual class KrossMapState {
    actual val currentLocation by mutableStateOf<KrossLocation?>(null)
    actual val markers = mutableStateListOf<KrossLocation>()
    actual val polylines = mutableStateListOf<List<KrossLocation>>()

    actual fun setCamera(location: KrossLocation){

    }
    actual fun addMarker(location: KrossLocation){

    }
    actual fun addPolyLine(list:List<KrossLocation>){

    }
}

@Composable
actual fun rememberKrossMapState(): KrossMapState {
    val locationState = remember { KrossMapState() }
    return locationState
}