package com.farimarwat.krossmap.core

data class KrossMapProperties(
    val showTraffic: Boolean,
    val showCompass: Boolean,
    val showBuildings: Boolean,
    val showPointOfInterest: Boolean, // Works Only with iOS
    val enableRotationGesture: Boolean,
    val enableTiltGesture: Boolean,
    val enableScrollGesture: Boolean,
)
