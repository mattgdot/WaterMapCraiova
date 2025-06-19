package com.app.water4craiova.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomAlertDialog(
    title:String,
    confirmText:String,
    dismissText:String,
    icon:ImageVector,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (() -> Unit)
) {
    AlertDialog(icon = {
        Icon(icon, contentDescription = null)
    }, title = {
        Text(text = title)
    }, text = {
        content()
    }, onDismissRequest = {
        onDismiss()
    }, dismissButton = {
        TextButton(onClick = {
            onDismiss()
        }) {
            Text(dismissText)
        }
    }, confirmButton = {
        TextButton(onClick = {
            onConfirm()
        }) {
            Text(confirmText)
        }
    })
}