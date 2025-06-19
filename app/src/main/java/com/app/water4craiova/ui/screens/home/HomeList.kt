package com.app.water4craiova.ui.screens.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.Location.distanceBetween
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.app.water4craiova.R
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.utils.Constants.INT_ZOOM
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

@Composable
fun HomeList(
    location: Location?,
    sourceList: List<WaterSource>,
    onSourceClick: (WaterSource) -> Unit,
    modifier: Modifier = Modifier
) {
    var localList by remember {
        mutableStateOf(sourceList)
    }

    if (location != null) {
        localList = sourceList.sortedBy { source ->
            SphericalUtil.computeDistanceBetween(
                LatLng(source.latitude, source.longitude),
                LatLng(location.latitude, location.longitude),
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = sourceList.size.toString() + " " + stringResource(id = R.string.locations),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(10.dp)
                )

            }
            items(
                localList,
            ) { source ->
                ListItem(headlineContent = {
                    Text(text = source.name)
                }, supportingContent = {
                    Text(text = source.subtitle)
                }, trailingContent = {
                    Text(
                        text = DecimalFormat("#.##").format(
                            (SphericalUtil.computeDistanceBetween(
                                LatLng(source.latitude, source.longitude),
                                LatLng(location.latitude, location.longitude),
                            ) / 1000)
                        ) + " km",
                    )
                }, leadingContent = {
                    Icon(
                        painterResource(id = R.drawable.fountain),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }, modifier = Modifier.clickable {
                    onSourceClick(source)
                })
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.noLocation),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}