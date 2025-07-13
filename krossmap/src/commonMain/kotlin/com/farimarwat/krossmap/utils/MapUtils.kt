package com.farimarwat.krossmap.utils

import com.farimarwat.krossmap.model.KrossCoordinate


import kotlin.math.PI

fun Double.toRadians(): Double = this * (PI / 180)
expect fun calculateBearing(start: KrossCoordinate, end: KrossCoordinate): Float