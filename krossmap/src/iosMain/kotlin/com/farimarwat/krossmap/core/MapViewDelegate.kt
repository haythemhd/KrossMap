package com.farimarwat.krossmap.core

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.QuartzCore.CATransform3DConcat
import platform.QuartzCore.CATransform3DMakeRotation
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.darwin.NSObject
import kotlin.math.PI

class MapViewDelegate(
    private val mapState: KrossMapState,
    private val cameraState: KrossCameraPositionState
    ) : NSObject(), MKMapViewDelegateProtocol {
    private var mapView: MKMapView? = null

    // Store reference to map view
    fun setMapView(mapView: MKMapView) {
        this.mapView = mapView
    }

    // Corrected method signature with MKAnnotationProtocol
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override fun mapView(mapView: MKMapView, viewForAnnotation: MKAnnotationProtocol): MKAnnotationView? {
        if (viewForAnnotation is MKPointAnnotation) {
            println("MKAnnotation Protocol")
            val identifier = "CustomMarker"
            var annotationView = mapView.dequeueReusableAnnotationViewWithIdentifier(identifier)

            if (annotationView == null) {
                annotationView = MKAnnotationView(viewForAnnotation, identifier)
                annotationView.canShowCallout = true
            } else {
                annotationView.annotation = viewForAnnotation
            }

            // Get the icon data from the map using the title
            val title = viewForAnnotation.title()
            val annotation = mapState.markers.firstOrNull{it.title == title}

            annotation?.icon?.let { data ->
                val nsData = data.usePinned { pinned ->
                    NSData.create(bytes = pinned.addressOf(0), length = data.size.toULong())
                }
                val uiImage = UIImage.imageWithData(nsData)
                annotationView.image = uiImage
                annotationView.setFrame(CGRectMake(0.0, 0.0, 40.0, 40.0))
            }

            return annotationView
        }
        return null
    }
    @OptIn(ExperimentalForeignApi::class)
    fun updateMarkerTransforms() {
        mapView?.let { map ->
            val cameraPitch = map.camera.pitch
            map.annotations.forEach { annotation ->
                if (annotation is MKPointAnnotation) {
                    val annotationView = map.viewForAnnotation(annotation)
                    annotationView?.let { view ->
                        mapState.currentLocation?.let { location ->
                            val bearing = if(cameraState.cameraFollow) 0f else location.bearing
                            val adjustedBearing = bearing + 90.0
                            val bearingRadians = adjustedBearing * PI / 180.0
                            val pitchRadians = -cameraPitch * 2.0 * PI / 180.0  // Multiply by 2 for more pronounced effect

                            val bearingTransform = CATransform3DMakeRotation(bearingRadians, 0.0, 0.0, 1.0)
                            val pitchTransform = CATransform3DMakeRotation(pitchRadians, 1.0, 0.0, 0.0)
                            view.transform3D = CATransform3DConcat(bearingTransform, pitchTransform)
                        }
                    }
                }
            }
        }
    }


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