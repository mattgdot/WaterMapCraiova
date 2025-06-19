package com.app.water4craiova.ui.screens.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.SatelliteAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.app.water4craiova.R
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.utils.Constants.INT_ZOOM
import com.app.water4craiova.utils.Constants.MAX_ZOOM
import com.app.water4craiova.utils.Constants.MIN_ZOOM
import com.app.water4craiova.utils.Constants.ROMANIA_BOUNDS
import com.app.water4craiova.utils.ThemeMode

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun HomeMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    location: Location?,
    recenterMap: Boolean,
    onSetRecenterMap: (Boolean) -> Unit,
    sourceList: List<WaterSource>,
    appTheme: ThemeMode,
    onSourceClick: (WaterSource) -> Unit,
    mapType: Int,
    onSetMapType: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    var isMapLoaded by remember { mutableStateOf(false) }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false
            )
        )
    }

    var viewMapType by rememberSaveable {
        mutableIntStateOf(mapType)
    }

    val mapColorScheme = when (appTheme) {
        ThemeMode.AUTO -> ComposeMapColorScheme.FOLLOW_SYSTEM
        ThemeMode.LIGHT -> ComposeMapColorScheme.LIGHT
        ThemeMode.DARK -> ComposeMapColorScheme.DARK
    }

    val context = LocalContext.current

    fun zoomTo(latLng: LatLng, zoomLevel: Float = MIN_ZOOM, animate: Boolean = false) {
        if (animate) {
            scope.launch {
                withContext(Dispatchers.Main) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng, zoomLevel
                        )
                    )
                }
            }
        } else {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        }
    }

    LaunchedEffect(location, recenterMap) {
        if (location != null && recenterMap) {
            val latLng = LatLng(
                location.latitude, location.longitude
            )
            zoomTo(latLng, INT_ZOOM, false)
        }
    }

    LaunchedEffect(cameraPositionState.cameraMoveStartedReason) {
        if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
            onSetRecenterMap(false)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMap(modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            properties = MapProperties(
                isMyLocationEnabled = location != null,
                mapType = if (viewMapType == 0) MapType.NORMAL else MapType.SATELLITE,
            ),
            mapColorScheme = mapColorScheme,
            onMapLoaded = {
                isMapLoaded = true
            }) {
            val iconMarkerFountain = bitmapDescriptorFromVector(context, R.drawable.fountain, 45, 45)
            val iconMarkerUnconfirmed = bitmapDescriptorFromVector(context, R.drawable.unconfirmed, 45, 45)

            sourceList.forEach { src ->
                Marker(
                    state = MarkerState(position = LatLng(src.latitude, src.longitude)),
                    title = src.name,
                    snippet = src.subtitle,
                    onClick = {
                        zoomTo(
                            LatLng(
                                src.latitude, src.longitude
                            ), MAX_ZOOM,true
                        )
                        onSourceClick(src)
                        true
                    },
                    icon = if(src.approved) iconMarkerFountain else iconMarkerUnconfirmed
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp)
                .padding(horizontal = 15.dp)
        ) {
            if (location != null) {
                FloatingActionButton(modifier = Modifier.padding(bottom = 20.dp), onClick = {
                    onSetRecenterMap(true)

                }) {
                    Icon(
                        if (recenterMap) Icons.Outlined.MyLocation else Icons.Outlined.LocationSearching,
                        null
                    )
                }
            }

            FloatingActionButton(onClick = {
                viewMapType = if (viewMapType == 0) 1 else 0
                onSetMapType(viewMapType)
            }) {
                Icon(
                    if (viewMapType == 0) Icons.Outlined.SatelliteAlt else Icons.Outlined.Map, null
                )
            }
        }
    }
}

fun bitmapDescriptorFromVector(
    context: Context, vectorResId: Int, widthDp: Int, heightDp: Int
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bm)
    drawable.draw(canvas)

    val scale = context.resources.displayMetrics.density
    val widthPx = (widthDp * scale + 0.5f).toInt()
    val heightPx = (heightDp * scale + 0.5f).toInt()

    val scaledBitmap = Bitmap.createScaledBitmap(bm, widthPx, heightPx, false)

    return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}