package com.farimarwat.krossmap.model

import androidx.compose.ui.graphics.Color

data class KrossPolyLine(
    val points:List<KrossLocation>,
    val color: Color,
    val width: Float
)
