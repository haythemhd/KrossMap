package com.farimarwat.krossmap.model

import org.jetbrains.compose.resources.DrawableResource

data class KrossMarker(
    var coordinate: KrossCoordinate,
    val title: String = "",
    val icon: DrawableResource? = null
)
