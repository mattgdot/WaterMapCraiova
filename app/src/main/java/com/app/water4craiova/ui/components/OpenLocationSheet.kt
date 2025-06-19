package com.app.water4craiova.ui.components

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.water4craiova.R
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.utils.Constants.PRIVACY_LINK
import com.app.water4craiova.utils.Constants.WEBVIEW_ROUTE
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.net.URLEncoder
import java.nio.file.WatchKey
import java.text.DecimalFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenLocationSheet(
    onDismiss: () -> Unit,
    onOpen: () -> Unit,
    onReport: (String) -> Unit,
    source: WaterSource,
    location: Location?,
    onImageClick: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        true
    )

    var distance = ""

    if (location != null) {
        distance = DecimalFormat("#.##").format(
            (SphericalUtil.computeDistanceBetween(
                LatLng(source.latitude, source.longitude),
                LatLng(location.latitude, location.longitude),
            ) / 1000)
        ) + " km | "

    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() },
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (source.images.isNotEmpty()) {
                AsyncImage(
                    source.images[0],
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .height(150.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(8.dp)).clickable {
                            onImageClick(source.images[0])
                            onDismiss()
                        },
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Text(
                text = source.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontSize = TextUnit(23f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )

            Text(
                text = distance + source.subtitle,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedButton(
                    onClick = { onOpen() }, modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                ) {
                    Text(text = stringResource(id = R.string.openWith))
                }

                OutlinedButton(
                    onClick = {
                        onReport(source.id)
                        onDismiss()
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                ) {
                    Text(text = stringResource(id = R.string.report))
                }
            }
        }
    }
}
