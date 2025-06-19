package com.app.water4craiova.ui.screens.report

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.water4craiova.R
import com.app.water4craiova.ui.components.AuthField
import com.app.water4craiova.ui.components.HeadingTextComponent
import com.app.water4craiova.ui.components.MyImageArea
import com.app.water4craiova.utils.Constants.ROMANIA_BOUNDS
import com.app.water4craiova.utils.Constants.ROMANIA_LOCATION
import com.app.water4craiova.utils.Constants.LOGIN_ROUTE
import com.app.water4craiova.utils.Constants.MIN_ZOOM
import com.app.water4craiova.utils.ThemeMode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun ReportForm(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
    openScreen: (String) -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
    appTheme: ThemeMode
) {
    val markerState = rememberMarkerState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ROMANIA_LOCATION, 13f)
    }
    val location = viewModel.location

    val user = viewModel.user.collectAsState(initial = null).value

    val scope = rememberCoroutineScope()

    fun zoomTo(latLng: LatLng, zoomLevel: Float = MIN_ZOOM) {
        scope.launch {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, zoomLevel
                ), 1000
            )
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            if (user.id.isBlank()) {
                openScreen(LOGIN_ROUTE)
            }
        }
    }

    LaunchedEffect(location) {
        if (location != null) zoomTo(LatLng(location.latitude, location.longitude), 15f)
    }

    LaunchedEffect(cameraPositionState.position) {
        markerState.position = cameraPositionState.position.target
    }

    val conf = LocalConfiguration.current
    val height = conf.screenHeightDp.dp

    var isMapLoaded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (!viewModel.loading) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            if (!viewModel.sent) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(height * 35 / 100)
                ) {
                    GoogleMap(modifier = modifier.matchParentSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = location != null,
                            latLngBoundsForCameraTarget = ROMANIA_BOUNDS,
                        ),
                        uiSettings = MapUiSettings(
                            myLocationButtonEnabled = true, compassEnabled = false
                        ),
                        mapColorScheme = when (appTheme) {
                            ThemeMode.AUTO -> ComposeMapColorScheme.FOLLOW_SYSTEM
                            ThemeMode.LIGHT -> ComposeMapColorScheme.LIGHT
                            ThemeMode.DARK -> ComposeMapColorScheme.DARK
                        },
                        onMapLoaded = {
                            isMapLoaded = true
                        }) {
                        Marker(
                            state = markerState,
                            draggable = true,
                        )
                    }

                    if (location == null || !isMapLoaded) {
                        androidx.compose.animation.AnimatedVisibility(
                            modifier = Modifier.matchParentSize(),
                            visible = !isMapLoaded,
                            enter = EnterTransition.None,
                            exit = fadeOut()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .wrapContentSize()
                            )
                        }
                    }
                }

                LazyColumn {
                    if (!viewModel.loading) {

                        item {
                            Text(
                                stringResource(id = R.string.dragMap),
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )

                            AuthField(
                                onUpdate = viewModel::updateName,
                                label = stringResource(id = R.string.sourceName),
                                icon = Icons.Outlined.Title,
                                error = viewModel.nameError,
                                singleLine = true,
                                maxLines = 1
                            )

                            AuthField(
                                onUpdate = viewModel::updateDetails,
                                label = stringResource(id = R.string.sourceDesc),
                                icon = Icons.Outlined.Description,
                                error = "",
                                singleLine = true,
                                maxLines = 1
                            )

                            MyImageArea(
                                directory = File(context.cacheDir, "images"),
                                uri = viewModel.photoUri,
                                onSetUri = viewModel::updatePhotoUri
                            )

                            Button(
                                onClick = {
                                    viewModel.updateAddress(
                                        LatLng(
                                            markerState.position.latitude,
                                            markerState.position.longitude
                                        )
                                    )
                                    viewModel.sendResponse()

                                }, modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(stringResource(id = R.string.send))
                            }
                        }

                    }
                }

            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    HeadingTextComponent(value = stringResource(id = R.string.requestReceived))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.requestReview),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { goBack() }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.done))
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
