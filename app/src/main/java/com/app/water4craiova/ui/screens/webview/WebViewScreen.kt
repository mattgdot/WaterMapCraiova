package com.app.water4craiova.ui.screens.webview

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(modifier: Modifier = Modifier, url: String) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true // Usewide viewport
            settings.loadWithOverviewMode = true // Enable overview mode
            if (url.contains("google")) {
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
            }

            loadUrl(url) // Load the URL without CSS
        }
    }, update = {
        it.loadUrl(url) // Reload the URL without CSS
    })
}