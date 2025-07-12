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
    private val reducePolyWidthBy = 12

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
                polylineRenderer.lineWidth = krossPolyline.width.minus(reducePolyWidthBy).toDouble()
            } else {
                // Default styling if no match found
                polylineRenderer.strokeColor = UIColor.blueColor
                polylineRenderer.lineWidth = 5.0 // Set a default width
            }

            return polylineRenderer
        }

        // Return a default renderer for other overlay types
        return MKOverlayRenderer(rendererForOverlay)
    }
}