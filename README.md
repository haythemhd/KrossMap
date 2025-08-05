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
    implementation("io.github.farimarwat:krossmap:1.3")
}

```

# 📍 KrossMap: Getting Started Guide

Welcome to **KrossMap**, a Kotlin Multiplatform mapping library designed for modern Compose applications.

This guide will walk you through the **basic usage** of `KrossMap`, including setting up the map, controlling the camera, adding markers and polylines, receiving location updates, and enabling 3D tilt view.

---

## 🧭 1. Setup Camera & Map State

Before rendering the map, you need to initialize two states:

- `KrossMapState`: manages markers, polylines, and location.
- `KrossCameraPositionState`: controls camera tilt, zoom, and animations.

```kotlin
// Initialize camera and map state
val mapState = rememberKrossMapState()
val cameraState = rememberKrossCameraPositionState(
    latitude = 32.60370,
    longitude = 70.92179,
    zoom = 17f,
    cameraFollow = true
)
```

---

## 🗺️ 2. Show the Map

Use the `KrossMap` composable to render the map using the states.

```kotlin
KrossMap(
    modifier = Modifier.fillMaxSize(),
    mapState = mapState,
    cameraPositionState = cameraState,
    properties = KrossMapPropertiesDefaults.defaults(),
    mapSettings = {
        //This can be your own composable which will be drawn on bottom-right
        MapSettings(
            tilt = cameraState.tilt,
            navigation = navigation,
            onCurrentLocationClicked = {
                mapState.requestCurrentLocation()
            },
            toggle3DViewClicked = {
                scope.launch {
                    cameraState.tilt = if (cameraState.tilt > 0) 0f else 60f
                    cameraState.animateCamera(tilt = cameraState.tilt)
                }
            },
            toggleNavigation = {
                navigation = !navigation
            }
        )
    }
)
```

---

## 📌 3. Add a Marker

You can add a marker to any coordinate using `KrossMarker`.

```kotlin
val marker = KrossMarker(
    coordinate = KrossCoordinate(32.60370, 70.92179),
    title = "My Marker",
    icon = Res.readBytes("drawable/ic_tracker.png")
)

mapState.addOrUpdateMarker(marker)
```

To update it dynamically (e.g. with location changes):

```kotlin
mapState.onUpdateLocation = { newCoord ->
    val updatedMarker = marker.copy(coordinate = newCoord)
    mapState.addOrUpdateMarker(updatedMarker)
}
```

---

## 📈 4. Draw a Polyline

Polylines can represent routes or paths.

```kotlin
val polyline = KrossPolyLine(
    points = listOf(
        KrossCoordinate(32.6037, 70.9215),
        KrossCoordinate(32.6038, 70.9220),
        KrossCoordinate(32.6039, 70.9225),
    ),
    title = "Route Path",
    color = Color.Blue,
    width = 50f
)

mapState.addPolyLine(polyline)
```

---

## 📡 5. Get Current Location Updates

To track user movement in real-time:

```kotlin
// Starts automatic location updates
mapState.startLocationUpdate()

// Stops updates when not needed
mapState.stopLocationUpdate()

// Callback when location changes
mapState.onUpdateLocation = { newLocation ->
    // Example: move marker to new location
    val updated = currentMarker.copy(coordinate = newLocation)
    mapState.addOrUpdateMarker(updated)
}
```

You can also request a one-time location:

```kotlin
mapState.requestCurrentLocation()
```

---

## 🎛️ 6. Enable 3D View (Tilt)

To toggle 3D tilt effect on the camera:

```kotlin
scope.launch {
    cameraState.tilt = if (cameraState.tilt > 0) 0f else 60f
    cameraState.animateCamera(tilt = cameraState.tilt)
}
```

You can bind this to a button to allow toggling:

```kotlin
IconButton(onClick = {
    scope.launch {
        cameraState.tilt = if (cameraState.tilt > 0) 0f else 60f
        cameraState.animateCamera(tilt = cameraState.tilt)
    }
}) {
    Icon(...) // Use a 3D icon
}
```

---

✅ That's it! You've now covered:

- Camera & map state setup  
- Displaying the map  
- Adding markers & polylines  
- Location tracking  
- 3D tilt camera view

Explore more by checking out the individual class documentation or experimenting further in your app!

---

### 🤝 Contribute

KrossMap is open-source and welcomes contributions!  
If you find a bug, have a feature request, or want to improve the library, feel free to open an issue or submit a pull request.

Check the [issues section](https://github.com/farimarwat/krossmap/issues) for things you can help with.

---

### 👨‍💻 About Me

Hi, I’m **Farman Ullah Khan** – a passionate Android & Kotlin Multiplatform developer.  
I love building open-source tools that simplify cross-platform development and improve developer experience.

You can connect with me on [LinkedIn](https://www.linkedin.com/in/farman-ullah-marwat-a02859196/) or check out my other projects on [GitHub](https://github.com/farimarwat).

