package com.farimarwat.krossmap.model

import androidx.compose.ui.graphics.Color
/**
 * Represents a polyline on the map, made up of multiple connected coordinates.
 *
 * @property points A list of [KrossCoordinate] representing the path of the polyline.
 * @property title An optional title or label associated with the polyline. Default is an empty string.
 * @property color The color of the polyline used for rendering on the map.
 * @property width The width (thickness) of the polyline in pixels.
 */
data class KrossPolyLine(
    val points: List<KrossCoordinate>,
    val title: String = "",
    val color: Color,
    val width: Float
)

