package com.example.demominiapp.sdk

import android.webkit.JavascriptInterface
import android.webkit.WebView

// Mediator giữa Web ↔ Native
abstract class MiniAppMessageBridge {

    private var webView: WebView? = null

    fun setWebView(webView: WebView) {
        this.webView = webView
        webView.addJavascriptInterface(this, "MiniAppBridge")
    }

    @JavascriptInterface
    open fun postMessage(message: String) {
        handleJsMessage(message)
    }

    @JavascriptInterface
    fun getUniqueId() {
        getUniqueId { uniqueId ->
            postJsCallback("onGetUniqueId", uniqueId)
        }
    }

    @JavascriptInterface
    fun requestCameraPermission() {
        requestDevicePermission(MiniAppDevicePermissionType.CAMERA) { granted ->
            postJsCallback("onCameraPermissionResult", granted.toString())
        }
    }

    private fun postJsCallback(functionName: String, result: String) {
        val js = "javascript:$functionName('$result')"
        webView?.post {
            webView?.evaluateJavascript(js, null)
        }
    }

    open fun notifyWebViewIsReady() {}

    open fun handleJsMessage(message: String) {}
    abstract fun getUniqueId(callback: (String) -> Unit)
    abstract fun requestDevicePermission(
        type: MiniAppDevicePermissionType,
        callback: (Boolean) -> Unit
    )
}
