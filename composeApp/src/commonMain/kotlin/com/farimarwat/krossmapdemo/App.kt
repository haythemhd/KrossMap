package com.farimarwat.krossmapdemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.farimarwat.krossmap.core.KrossMap
import com.farimarwat.krossmap.core.rememberKrossCameraPositionState
import com.farimarwat.krossmap.core.rememberKrossMapState
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine
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
            val polyline = KrossPolyLine(
                points = Coordinates.coordinates.map { (lon, lat) -> KrossCoordinate(latitude = lat, longitude = lon) },
                title = "Route",
                color = Color.Blue,
                width = 24f
            )

            mapState.addPolyLine(polyline)
            KrossMap(
                modifier = Modifier.fillMaxSize(),
                mapState = mapState,
                cameraPositionState = cameraState,
            )
        }
    }
}

object Coordinates {
    val coordinates = listOf(
        listOf(70.921562, 32.603839),
        listOf(70.921122, 32.60389),
        listOf(70.920083, 32.603916),
        listOf(70.919105, 32.603881),
        listOf(70.918896, 32.60384),
        listOf(70.918499, 32.6037),
        listOf(70.918338, 32.603624),
        listOf(70.91815, 32.603497),
        listOf(70.917946, 32.603217),
        listOf(70.91773, 32.602963),
        listOf(70.91763, 32.603047),
        listOf(70.916821, 32.603646),
        listOf(70.916305, 32.604093),
        listOf(70.915958, 32.603738),
        listOf(70.915245, 32.602931),
        listOf(70.915054, 32.602679),
        listOf(70.914918, 32.60257),
        listOf(70.914758, 32.602518),
        listOf(70.91461, 32.602482),
        listOf(70.914412, 32.602449),
        listOf(70.914097, 32.602449),
        listOf(70.913887, 32.602473),
        listOf(70.913119, 32.602611),
        listOf(70.912232, 32.602819),
        listOf(70.910445, 32.603681),
        listOf(70.908691, 32.604489),
        listOf(70.9084, 32.604641),
        listOf(70.908203, 32.604867),
        listOf(70.907469, 32.606122),
        listOf(70.906835, 32.60689),
        listOf(70.906525, 32.607528),
        listOf(70.90641, 32.607699),
        listOf(70.906166, 32.608026),
        listOf(70.90668, 32.608314),
        listOf(70.906977, 32.608495),
        listOf(70.906483, 32.608583)
    )
}