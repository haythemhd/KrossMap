package com.farimarwat.krossmap.model

import androidx.compose.ui.graphics.Color

data class KrossPolyLine(
    val points:List<KrossCoordinate>,
    val title: String = "",
    val color: Color,
    val width: Float
)
