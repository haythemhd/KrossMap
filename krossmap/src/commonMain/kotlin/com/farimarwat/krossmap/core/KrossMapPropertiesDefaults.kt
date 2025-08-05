package com.farimarwat.krossmap.core

/**
 * Provides default values for [KrossMapProperties].
 */
object KrossMapPropertiesDefaults {

    /**
     * Returns a [KrossMapProperties] object with all features enabled.
     */
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