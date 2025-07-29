package com.farimarwat.krossmap.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine
import com.farimarwat.krossmap.utils.calculateBearing
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject

actual class KrossMapState {
    actual var currentLocation by mutableStateOf<KrossCoordinate?>(null)
    actual val markers: SnapshotStateList<KrossMarker> = mutableStateListOf()
    actual val polylines = mutableStateListOf<KrossPolyLine>()

    actual var onUpdateLocation:(KrossCoordinate)-> Unit = {  }

    private val locationManager = CLLocationManager()

    actual internal var previousCoordinates: KrossCoordinate? = null



    @OptIn(ExperimentalForeignApi::class)
    val locationManagerDelegate: CLLocationManagerDelegateProtocol =
        object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val location = (didUpdateLocations.lastOrNull() as? CLLocation) ?: return
                val coordinate = location.coordinate
                coordinate.useContents {
                    val bearing = if (previousCoordinates != null) {
                        calculateBearing(
                            start = KrossCoordinate(previousCoordinates!!.latitude, previousCoordinates!!.longitude),
                            end = KrossCoordinate(latitude, longitude)
                        )
                    } else {
                        0f
                    }

                    val newCoordinates = KrossCoordinate(latitude,longitude,bearing)
                    println(newCoordinates)
                    onUpdateLocation.invoke(newCoordinates)
                    currentLocation = newCoordinates
                    if (currentLocationRequested) {
                        stopLocationUpdate()
                        currentLocationRequested = false
                    }

                    previousCoordinates = newCoordinates
                }


            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                println("Location update failed: ${didFailWithError.localizedDescription}")
            }

            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: CLAuthorizationStatus
            ) {
                when (didChangeAuthorizationStatus) {
                    kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                        manager.startUpdatingLocation()
                    }

                    else -> {
                        println("Permission not granted: $didChangeAuthorizationStatus")
                    }
                }
            }
        }

    actual fun addOrUpdateMarker(marker: KrossMarker) {
        val existingIndex = markers.indexOfFirst { it.title == marker.title }
        if (existingIndex != -1) {
            markers[existingIndex] = marker
        } else {
            markers.add(marker)
        }
    }

    actual fun removeMarker(marker: KrossMarker) {
        markers.remove(marker)
    }

    actual fun addPolyLine(polyLine: KrossPolyLine) {
        polylines.add(polyLine)
    }

    actual fun removePolyLine(polyline: KrossPolyLine) {
        polylines.remove(polyline)
    }

    internal actual var currentLocationRequested: Boolean = false

    actual fun requestCurrentLocation() {
        currentLocationRequested = true
        startLocationUpdate()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun startLocationUpdate() {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.delegate = locationManagerDelegate
        locationManager.distanceFilter = 3.0
        val currentStatus = locationManager.authorizationStatus
        when (currentStatus) {
            kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                locationManager.startUpdatingLocation()
            }

            else -> {
                locationManager.requestWhenInUseAuthorization()
            }
        }
    }

    actual fun stopLocationUpdate() {
        locationManager.stopUpdatingLocation()
    }


}

@Composable
actual fun rememberKrossMapState(): KrossMapState {
    val locationState = remember { KrossMapState() }
    return locationState
}