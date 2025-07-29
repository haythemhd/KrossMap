package com.farimarwat.krossmap.utils

import com.farimarwat.krossmap.model.KrossCoordinate
import kotlin.math.*

actual fun calculateBearing(
    start: KrossCoordinate,
    end: KrossCoordinate
) : Float {
    val lat1 = start.latitude.toRadians()
    val lon1 = start.longitude.toRadians()
    val lat2 = end.latitude.toRadians()
    val lon2 = end.longitude.toRadians()

    val dLon = lon2 - lon1
    val y = sin(dLon) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
    val bearing = atan2(y, x)

    var bearingDegrees = ((bearing.toDegrees() + 360) % 360).toFloat()

    // Adjust for iOS: Your image points North at 0°, but iOS 0° is East
    // So subtract 90° to align properly
    bearingDegrees = (bearingDegrees - 90f + 360f) % 360f

    return bearingDegrees
}
fun Double.toDegrees() = this * 180 / PI