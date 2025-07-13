package com.farimarwat.krossmap.utils

import com.farimarwat.krossmap.model.KrossCoordinate
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

actual fun calculateBearing(
    start: KrossCoordinate,
    end: KrossCoordinate
): Float {
    return SphericalUtil.computeHeading(
        LatLng(start.latitude, start.longitude),
        LatLng(end.latitude, end.longitude)
    ).toFloat()
}