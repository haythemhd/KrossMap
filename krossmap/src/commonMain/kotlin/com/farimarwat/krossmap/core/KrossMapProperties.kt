package com.farimarwat.krossmap.core


/**
 * Represents customizable map display and interaction properties.
 *
 * @property showTraffic Whether to display real-time traffic data.
 * @property showCompass Whether to show the compass on the map UI.
 * @property showBuildings Whether to render 3D buildings on the map (if supported).
 * @property showPointOfInterest Whether to show points of interest (iOS only).
 * @property enableRotationGesture Whether map rotation via gestures is allowed.
 * @property enableTiltGesture Whether tilt gestures are enabled.
 * @property enableScrollGesture Whether scroll/pan gestures are enabled.
 */
data class KrossMapProperties(
    val showTraffic: Boolean,
    val showCompass: Boolean,
    val showBuildings: Boolean,
    val showPointOfInterest: Boolean, // Works Only with iOS
    val enableRotationGesture: Boolean,
    val enableTiltGesture: Boolean,
    val enableScrollGesture: Boolean,
)