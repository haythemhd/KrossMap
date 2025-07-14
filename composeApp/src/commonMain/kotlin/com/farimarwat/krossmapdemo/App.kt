package com.farimarwat.krossmapdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.farimarwat.krossmap.core.KrossMap
import com.farimarwat.krossmap.core.rememberKrossCameraPositionState
import com.farimarwat.krossmap.core.rememberKrossMapState
import com.farimarwat.krossmap.model.KrossCoordinate
import com.farimarwat.krossmap.model.KrossMarker
import com.farimarwat.krossmap.model.KrossPolyLine
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import krossmapdemo.composeapp.generated.resources.Res
import krossmapdemo.composeapp.generated.resources.ic_3d
import krossmapdemo.composeapp.generated.resources.ic_3d_view
import krossmapdemo.composeapp.generated.resources.ic_3d_view_cross
import krossmapdemo.composeapp.generated.resources.ic_current_location
import krossmapdemo.composeapp.generated.resources.ic_direction
import krossmapdemo.composeapp.generated.resources.ic_direction_cross
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        val latitude = 32.60370
        val longitude = 70.92179
        val zoom = 17f
        var tilt by remember { mutableStateOf(60f)}

        val currentLocationMarker = remember {
            mutableStateOf<KrossMarker?>(null)
        }

        var navigation by remember { mutableStateOf(false)}

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val scope = rememberCoroutineScope()

            var permissionGranted by remember { mutableStateOf(false) }
            val permissionFactory = rememberPermissionsControllerFactory()
            val permissionController = remember(permissionFactory) {
                permissionFactory.createPermissionsController()
            }
            BindEffect(permissionController)
            LaunchedEffect(Unit) {
                permissionGranted = permissionController.isPermissionGranted(Permission.LOCATION)
                if (!permissionGranted) {
                    try {
                        permissionController.providePermission(Permission.LOCATION)
                        permissionGranted =
                            permissionController.isPermissionGranted(Permission.LOCATION)
                    } catch (ex: DeniedException) {
                        permissionController.openAppSettings()
                        println(ex)
                    } catch (ex: DeniedAlwaysException) {
                        permissionController.openAppSettings()
                        println(ex)
                    } catch (ex: RequestCanceledException) {
                        println(ex)
                    }
                } else {
                    println("Permission already granted")
                }
            }


            //Create Map State
            val mapState = rememberKrossMapState()
            //Create Camera State
            val cameraState = rememberKrossCameraPositionState(
                latitude, longitude, zoom, tilt, 0f
            )

            LaunchedEffect(Unit) {
                currentLocationMarker.value =
                    KrossMarker(
                        coordinate = KrossCoordinate(latitude, longitude),
                        title = "Player",
                        icon = Res.readBytes("drawable/ic_tracker.png")
                    )
            }
            LaunchedEffect(Unit) {
                mapState.onUpdateLocation = {
                    currentLocationMarker.value = currentLocationMarker.value?.copy(coordinate = it)
                    currentLocationMarker.value?.let { cm ->
                        mapState.addOrUpdateMarker(cm)
                    }
                    scope.launch {
                        cameraState.animateCamera(it.latitude, it.longitude, it.bearing)
                    }
                }
            }
            LaunchedEffect(navigation){
                if(navigation){
                    mapState.startLocationUpdate()
                } else {
                    mapState.stopLocationUpdate()
                }
            }

            //Animate camera when camera position change is detected
            LaunchedEffect(cameraState.currentCameraPosition) {
                cameraState.currentCameraPosition?.let { position ->
                    println("MyPosition: ${position}")
                    cameraState.animateCamera(
                        position.latitude,
                        position.longitude
                    )
                }
            }
            LaunchedEffect(currentLocationMarker) {
                currentLocationMarker.value?.let { cm ->
                    mapState.addOrUpdateMarker(cm)
                }
            }

            //Add PolyLine
            LaunchedEffect(Unit) {
                val polyline = KrossPolyLine(
                    points = Coordinates.coordinates.map { (lon, lat) ->
                        KrossCoordinate(
                            latitude = lat,
                            longitude = lon
                        )
                    },
                    title = "Route",
                    color = Color.Blue,
                    width = 50f
                )

                mapState.addPolyLine(polyline)
            }

            if (permissionGranted) {
                //Create Map
                KrossMap(
                    modifier = Modifier.fillMaxSize(),
                    mapState = mapState,
                    cameraPositionState = cameraState,
                    mapSettings = {
                        MapSettings(
                            tilt = tilt,
                            navigation = navigation,
                            onCurrentLocationClicked = {
                                mapState.requestCurrentLocation()
                            },
                            toggle3DViewClicked = {
                                tilt = if(tilt > 0 ){
                                    0f
                                } else {
                                    60f
                                }
                                scope.launch {
                                    cameraState.changeTilt(tilt)
                                }
                            },
                            toggleNavigation = {
                                navigation = !navigation
                            }
                        )
                    }
                )
            }

        }
    }
}

@Composable
fun MapSettings(
    tilt: Float,
    navigation: Boolean,
    onCurrentLocationClicked: () -> Unit = {},
    toggle3DViewClicked: () -> Unit = {},
    toggleNavigation:()-> Unit = {}
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Blue),
            onClick = onCurrentLocationClicked
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(Res.drawable.ic_current_location),
                contentDescription = "Current Location",
                tint = Color.White
            )
        }
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Blue),
            onClick = toggle3DViewClicked
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(
                    if(tilt > 0){
                        Res.drawable.ic_3d_view_cross
                    } else {
                        Res.drawable.ic_3d_view
                    }
                ),
                contentDescription = "Current Location",
                tint = Color.White
            )
        }
        IconButton(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Blue),
            onClick = toggleNavigation
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(
                    if(navigation){
                        Res.drawable.ic_direction_cross
                    } else {
                        Res.drawable.ic_direction
                    }
                ),
                contentDescription = "Current Location",
                tint = Color.White
            )
        }
    }
}

object Coordinates {
    val coordinates = listOf(
        listOf(70.921562, 32.603839),
        listOf(70.921122, 32.60389),
        listOf(70.920083, 32.603916),
        listOf(70.919105, 32.603881),
        listOf(70.918896, 32.60384),
        listOf(70.918499, 32.6037),
        listOf(70.918338, 32.603624),
        listOf(70.91815, 32.603497),
        listOf(70.917946, 32.603217),
        listOf(70.91773, 32.602963),
        listOf(70.91763, 32.603047),
        listOf(70.916821, 32.603646),
        listOf(70.916305, 32.604093),
        listOf(70.915958, 32.603738),
        listOf(70.915245, 32.602931),
        listOf(70.915054, 32.602679),
        listOf(70.914918, 32.60257),
        listOf(70.914758, 32.602518),
        listOf(70.91461, 32.602482),
        listOf(70.914412, 32.602449),
        listOf(70.914097, 32.602449),
        listOf(70.913887, 32.602473),
        listOf(70.913119, 32.602611),
        listOf(70.912232, 32.602819),
        listOf(70.910445, 32.603681),
        listOf(70.908691, 32.604489),
        listOf(70.9084, 32.604641),
        listOf(70.908203, 32.604867),
        listOf(70.907469, 32.606122),
        listOf(70.906835, 32.60689),
        listOf(70.906525, 32.607528),
        listOf(70.90641, 32.607699),
        listOf(70.906166, 32.608026),
        listOf(70.90668, 32.608314),
        listOf(70.906977, 32.608495),
        listOf(70.906483, 32.608583)
    )
}