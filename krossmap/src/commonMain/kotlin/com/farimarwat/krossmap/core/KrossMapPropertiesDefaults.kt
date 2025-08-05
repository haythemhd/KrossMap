package com.farimarwat.krossmap.core

object KrossMapPropertiesDefaults {

    fun defaults(): KrossMapProperties = KrossMapProperties(
        showTraffic = true,
        showCompass = true,
        showBuildings = true,
        showPointOfInterest = true,
        enableRotationGesture = true,
        enableTiltGesture = true,
        enableScrollGesture = true,
    )
}