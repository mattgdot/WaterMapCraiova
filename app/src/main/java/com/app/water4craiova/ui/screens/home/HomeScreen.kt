package com.app.water4craiova.ui.screens.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.app.water4craiova.LocalSnackbarHostState
import com.app.water4craiova.R
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.ui.components.OpenLocationSheet
import com.app.water4craiova.utils.Constants.ROMANIA_LOCATION
import com.app.water4craiova.utils.Constants.DOCS_LINK
import com.app.water4craiova.utils.Constants.REPORT_FORM_PATH
import com.app.water4craiova.utils.Constants.REPORT_ROUTE
import com.app.water4craiova.utils.Constants.WEBVIEW_ROUTE
import com.app.water4craiova.utils.Format.getUriFromCoordinates
import com.app.water4craiova.utils.ThemeMode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import java.net.URLEncoder

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(), appTheme: ThemeMode, openScreen: (String) -> Unit
) {
    var permissionRequested by rememberSaveable {
        mutableStateOf(false)
    }

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    ) {
        permissionRequested = true
    }

    val context = LocalContext.current

    val revokedPermissions =
        locationPermissionState.permissions.size == locationPermissionState.revokedPermissions.size

    LaunchedEffect(revokedPermissions) {
        viewModel.setIsLocationEnabled(!revokedPermissions)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && revokedPermissions && !locationPermissionState.shouldShowRationale) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    val snackbarHostState = LocalSnackbarHostState.current

    val location = viewModel.location
    val sources = viewModel.sources


    if (revokedPermissions && (locationPermissionState.shouldShowRationale || !locationPermissionState.shouldShowRationale && permissionRequested)) {
        LaunchedEffect(Unit) {
            val userAction = snackbarHostState.showSnackbar(
                message = context.getString(R.string.locationPermission),
                actionLabel = context.getString(R.string.enableLocation),
                duration = SnackbarDuration.Indefinite,
            )
            if (userAction == SnackbarResult.ActionPerformed) {
                if (locationPermissionState.shouldShowRationale) {
                    locationPermissionState.launchMultiplePermissionRequest()
                } else if (permissionRequested) {
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    ).also {
                        startActivity(context, it, null)
                    }
                }
            }
        }
    }

    var showDetailsSheet by remember {
        mutableStateOf(false)
    }

    var selectedSource by remember {
        mutableStateOf(WaterSource())
    }

    var selectedTabIndex by rememberSaveable {
        mutableStateOf(viewModel.defaultTab)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ROMANIA_LOCATION, 6f)
    }

    var recenterMap by rememberSaveable {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
            Tab(selected = selectedTabIndex == 0, onClick = {
                selectedTabIndex = 0
                viewModel.setSelectedTab(selectedTabIndex)
            }) {
                Text(
                    stringResource(id = R.string.list),
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)
                )
            }
            Tab(selected = selectedTabIndex == 1, onClick = {
                selectedTabIndex = 1
                viewModel.setSelectedTab(selectedTabIndex)
            }) {
                Text(
                    stringResource(id = R.string.map),
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (!revokedPermissions && location == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                if (selectedTabIndex == 0) {
                    HomeList(location = location, sourceList = sources, onSourceClick = { src ->
                        showDetailsSheet = true
                        selectedSource = src
                    })
                } else {
                    HomeMap(
                        cameraPositionState = cameraPositionState,
                        location = location,
                        onSetRecenterMap = {
                            recenterMap = it
                        },
                        recenterMap = recenterMap,
                        sourceList = sources,
                        appTheme = appTheme,
                        onSourceClick = { src ->
                            showDetailsSheet = true
                            selectedSource = src
                        },
                        mapType = viewModel.getMapType(),
                        onSetMapType = viewModel::setMapType
                    )
                }
            }
            if (location != null) {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(id = R.string.addSrc)) },
                    icon = { Icon(Icons.Outlined.Add, null) },
                    onClick = { openScreen(REPORT_ROUTE) },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 20.dp)
                        .padding(horizontal = 15.dp)
                )
            }
        }
    }

    if (showDetailsSheet) {
        OpenLocationSheet(onDismiss = { showDetailsSheet = false }, onOpen = {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    getUriFromCoordinates(
                        selectedSource.latitude, selectedSource.longitude
                    )
                )
            )
            context.startActivity(intent)
        }, onReport = {
            openScreen(
                WEBVIEW_ROUTE.replace(
                    "{url}", URLEncoder.encode(
                        DOCS_LINK + REPORT_FORM_PATH.replace("{SRC_ID}", it), "UTF-8"
                    )
                )
            )
        }, source = selectedSource, location = location, onImageClick = {
            openScreen(
                WEBVIEW_ROUTE.replace(
                    "{url}", URLEncoder.encode(it, "UTF-8")
                )
            )
        })
    }
}
