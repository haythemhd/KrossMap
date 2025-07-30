package com.farimarwat.krossmap.model

import org.jetbrains.compose.resources.DrawableResource

/**
 * Represents a marker on the map with a coordinate, optional title, and custom icon.
 *
 * @property coordinate The geographical position where the marker is placed.
 * @property title An optional title shown with the marker. Default is an empty string.
 * @property icon An optional image icon for the marker, represented as a byte array (e.g. PNG). Default is null.
 */
data class KrossMarker(
    var coordinate: KrossCoordinate,
    val title: String = "",
    val icon: ByteArray? = null
) {

    /**
     * Checks equality based on coordinate, title, and icon content.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as KrossMarker

        if (coordinate != other.coordinate) return false
        if (title != other.title) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    /**
     * Computes hash code using coordinate, title, and icon content.
     */
    override fun hashCode(): Int {
        var result = coordinate.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (icon?.contentHashCode() ?: 0)
        return result
    }
}

