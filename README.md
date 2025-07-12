## 🗺️ KrossMap

**KrossMap** is a lightweight, cross-platform Maps library designed for **Kotlin Multiplatform (KMP)**. It provides an easy and consistent API for working with maps, markers, polylines, and camera movements across Android and iOS — all using **Jetpack Compose** and **SwiftUI Compose Interop**.

Whether you're building a delivery app, ride tracker, or location-based feature, **KrossMap** simplifies the map experience with powerful abstractions and built-in utilities.

### 🚀 Features

- 🧭 Marker rendering & animation  
- 📍 Current location tracking  
- 📷 Camera control & animation  
- 🛣️ Polyline (route) support  
- 💡 Jetpack Compose friendly  
- 🌍 Kotlin Multiplatform Ready (Android & iOS)

### 📦 Dependency

To add **KrossMap** to your project, include the following in your **shared module's `build.gradle.kts`**:

```kotlin
dependencies {
    implementation("io.github.farimarwat:krossmap:1.0")
}

```

### 🛠️ Usage Guide

Follow these simple steps to get started with `KrossMap` in your Kotlin Multiplatform app.

---

### 1️⃣ Create Camera State

This defines the initial position and zoom level of the map:

```kotlin
val cameraState = rememberKrossCameraPositionState(
    latitude = 32.60370,
    longitude = 70.92179,
    zoom = 18f
)
```
### 2️⃣ Create Map State

```kotlin
val mapState = rememberKrossMapState()

LaunchedEffect(Unit) {
    mapState.startLocationUpdate()
    mapState.onUpdateLocation = { coordinates ->
        // Update marker and camera when location changes
       
    }
}
```

### 3️⃣ Add Marker (Optional or Initial)

```kotlin
LaunchedEffect(Unit) {
    val currentLocationMarker = remember {
        KrossMarker(
            coordinate = KrossCoordinate(32.60370, 70.92179),
            title = "Current"
        )
    }
    mapState.addOrUpdateMarker(currentLocationMarker)
}
```
### 4️⃣ Add Polyline (Route)

```kotlin
LaunchedEffect(Unit) {
    val polyline = KrossPolyLine(
        points = listOf(
            KrossCoordinate(32.60370, 70.92179),
            KrossCoordinate(32.60450, 70.92230),
            KrossCoordinate(32.60500, 70.92300),
            // Add more coordinates as needed
        ),
        title = "Route",
        color = Color.Blue,
        width = 24f
    )

    mapState.addPolyLine(polyline)
}
```
