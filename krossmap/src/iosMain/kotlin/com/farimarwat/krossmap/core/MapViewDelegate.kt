package com.farimarwat.krossmap.core

import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.UIKit.UIColor
import platform.darwin.NSObject

class MapViewDelegate(private val mapState: KrossMapState) : NSObject(), MKMapViewDelegateProtocol {

    /**
     * Alternative approach: Use a scaling factor based on empirical testing
     */
    private fun convertPolylineWidthSimple(androidWidth: Float): Double {
        // iOS polylines appear roughly 1.4x thicker than Android
        // So we divide by 1.4 to compensate
        return (androidWidth / 2.6)
    }


    override fun mapView(mapView: MKMapView, rendererForOverlay: MKOverlayProtocol): MKOverlayRenderer {
        if (rendererForOverlay is MKPolyline) {
            val polylineRenderer = MKPolylineRenderer(rendererForOverlay)

            // Find the corresponding KrossPolyLine to get styling info
            val krossPolyline = mapState.polylines.find { poly ->
                // Match by title or other identifier
                rendererForOverlay.title() == poly.title
            }

            if (krossPolyline != null) {
                // Apply styling from the found KrossPolyLine
                polylineRenderer.strokeColor = krossPolyline.color.toUIColor()

                // Choose one of the conversion methods:
                // Method 1: Dynamic scaling based on width ranges
                polylineRenderer.lineWidth = convertPolylineWidthSimple(krossPolyline.width)

                // Method 2: Simple scaling (uncomment to use)
                // polylineRenderer.lineWidth = convertPolylineWidthSimple(krossPolyline.width)

                // Method 3: Lookup table (uncomment to use)
                // polylineRenderer.lineWidth = convertPolylineWidthLookup(krossPolyline.width)

            } else {
                // Default styling if no match found
                polylineRenderer.strokeColor = UIColor.blueColor
                polylineRenderer.lineWidth = 5.0
            }

            return polylineRenderer
        }

        // Return a default renderer for other overlay types
        return MKOverlayRenderer(rendererForOverlay)
    }
}

/**
 * Extension function to make conversion reusable across your app
 */
fun Float.toiOSPolylineWidth(): Double {
    return when {
        this <= 2f -> this * 0.8
        this <= 5f -> this * 0.75
        this <= 10f -> this * 0.7
        this <= 20f -> this * 0.65
        else -> this * 0.6
    }.toDouble()
}

/**
 * Alternative extension with simple scaling
 */
fun Float.toiOSPolylineWidthSimple(): Double {
    return (this / 1.4).toDouble()
}