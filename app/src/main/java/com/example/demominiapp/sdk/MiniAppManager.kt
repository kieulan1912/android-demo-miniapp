package com.example.demominiapp.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import com.example.demominiapp.sdk.model.MiniAppInterface

object MiniAppManager : MiniAppInterface {

    @SuppressLint("SetJavaScriptEnabled")
    override fun create(
        context: Context,
        miniAppId: String,
        baseUrl: String,
        miniAppMessageBridge: MiniAppMessageBridge
    ): MiniAppDisplay {
        val webView = WebView(context)

        // Kích hoạt JavaScript
        webView.settings.javaScriptEnabled = true

        // Gắn Native Bridge vào WebView
        if (miniAppMessageBridge is CustomMiniAppMessageBridge) {
            miniAppMessageBridge.attachWebView(webView)
        }

        // Load Mini App từ server
        val url = "$baseUrl/miniapp/$miniAppId/v1/index.html"
        webView.loadUrl(url)

        return object : MiniAppDisplay {
            override fun getWebView(): WebView = webView
            override fun getMiniAppView(): WebView {
                return webView
            }
        }
    }
}
