package com.farimarwat.krossmapdemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.farimarwat.krossmap.core.KrossMap
import com.farimarwat.krossmap.core.rememberKrossCameraPositionState
import com.farimarwat.krossmap.core.rememberKrossMapState
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val latitude = 32.60248
            val longitude = 70.92092
            val zoom = 18f

            val mapState = rememberKrossMapState()
            val cameraState = rememberKrossCameraPositionState(
                latitude,longitude,zoom
            )
            mapState.addMarker(
                KrossMarker(
                    KrossCoordinate(latitude,longitude),
                    "Lakki Marwat"
                )
            )
            KrossMap(
                modifier = Modifier.fillMaxSize(),
                mapState = mapState,
                cameraPositionState = cameraState,
            )
        }
    }
}