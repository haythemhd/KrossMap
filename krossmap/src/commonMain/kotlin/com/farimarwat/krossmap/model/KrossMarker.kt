package com.farimarwat.krossmap.model

import org.jetbrains.compose.resources.DrawableResource

data class KrossMarker(
    var coordinate: KrossCoordinate,
    val title: String = "",
    val icon: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as KrossMarker

        if (coordinate != other.coordinate) return false
        if (title != other.title) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinate.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (icon?.contentHashCode() ?: 0)
        return result
    }
}
