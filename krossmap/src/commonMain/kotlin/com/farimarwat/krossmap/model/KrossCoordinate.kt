package com.farimarwat.krossmap.model

/**
 * Represents a geographical coordinate with optional bearing (direction).
 *
 * @property latitude The latitude component of the coordinate, in degrees.
 * @property longitude The longitude component of the coordinate, in degrees.
 * @property bearing The direction in which the camera or object is facing, in degrees. Default is 0f.
 */
data class KrossCoordinate(
    val latitude: Double,
    val longitude: Double,
    var bearing: Float = 0f
)
