package com.example.itemfinder.presentation.homescreen

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URISyntaxException


@Composable
fun OpenInWebViewScreen(url: String?) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    settings.javaScriptCanOpenWindowsAutomatically = true

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val requestUrl = request?.url.toString()

                            // Обработка intent:// или kaspi://
                            if (requestUrl.startsWith("kaspi://") || requestUrl.startsWith("intent://")) {
                                try {
                                    val intent = Intent.parseUri(requestUrl, Intent.URI_INTENT_SCHEME)
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=..."))
                                    // context.startActivity(fallbackIntent)
                                } catch (e: URISyntaxException) {
                                    e.printStackTrace()
                                }
                                return true
                            }
                            return false
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onCreateWindow(
                            view: WebView?,
                            isDialog: Boolean,
                            isUserGesture: Boolean,
                            resultMsg: Message?
                        ): Boolean {
                            val newWebView = view?.context?.let { WebView(it) }
                            newWebView?.settings?.javaScriptEnabled = true
                            newWebView?.settings?.domStorageEnabled = true
                            newWebView?.settings?.javaScriptCanOpenWindowsAutomatically = true
                            newWebView?.webViewClient = object : WebViewClient() {}

                            val dialog = Dialog(view!!.context)
                            newWebView?.let { dialog.setContentView(it) }
                            dialog.show()

                            (resultMsg?.obj as WebView.WebViewTransport).webView = newWebView
                            resultMsg.sendToTarget()
                            return true
                        }
                    }
                    loadUrl(url!!)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f)), // можно добавить затемнение
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }
    }
}

