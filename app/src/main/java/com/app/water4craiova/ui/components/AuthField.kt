package com.app.water4craiova.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AuthField(
    modifier: Modifier = Modifier,
    onUpdate: (String) -> Unit = {},
    label: String = "",
    icon: ImageVector = Icons.Outlined.Email,
    error: String = "",
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        value = text,
        onValueChange = {
            text = it
            onUpdate(it)
        },
        supportingText = {
            if (error.isNotEmpty()) Text(error)
        },
        label = { Text(label) },
        isError = error.isNotEmpty(),
        shape = RoundedCornerShape(13.dp),
        leadingIcon = {
            Icon(icon, contentDescription = null)
        },
        trailingIcon = {
            if (text.isNotEmpty()) IconButton(onClick = {
                text = ""
                onUpdate("")
            }) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        },
        singleLine = singleLine,
        maxLines = maxLines,
    )

}