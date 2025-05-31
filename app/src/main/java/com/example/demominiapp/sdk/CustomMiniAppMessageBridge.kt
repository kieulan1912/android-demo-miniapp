package com.example.demominiapp.sdk

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import com.example.demominiapp.MainActivity

class CustomMiniAppMessageBridge(
    private val context: Context
) : MiniAppMessageBridge() {

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 1001
    }

    private var permissionCallback: ((Boolean) -> Unit)? = null
    private var webView: WebView? = null
    var onMessageReceived: ((String) -> Unit)? = null // Callback cho WebViewActivity

    override fun getUniqueId(callback: (String) -> Unit) {
         TODO("Not yet implemented")
     }

    fun attachWebView(view: WebView) {
        webView = view
        view.addJavascriptInterface(this, "MiniAppBridge")
    }

    @JavascriptInterface
    override fun postMessage(message: String) {
        Log.d("MiniAppBridge", "Received message from JS: $message")
        MainActivity.updateMessageFromMiniApp(message)
        onMessageReceived?.invoke(message)
    }

    @JavascriptInterface
    fun requestDevicePermissionFromJs(type: String) {
        Log.d("MiniAppBridge", "JS requested permission: $type")

        val permissionType = try {
            MiniAppDevicePermissionType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            Log.e("MiniAppBridge", "Invalid permission type: $type")
            return
        }

        requestDevicePermission(permissionType) { granted ->
            sendCameraPermissionResultToWeb(granted)
        }
    }


    override fun requestDevicePermission(type: MiniAppDevicePermissionType, callback: (Boolean) -> Unit) {
        Log.d("CustomBridge", "Request permission: $type")

        if (type == MiniAppDevicePermissionType.CAMERA) {
            permissionCallback = callback
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (context is Activity) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    callback(false)
                }
            } else {
                callback(true)
            }
        } else {
            callback(false)
        }
    }

    private fun sendCameraPermissionResultToWeb(granted: Boolean) {
        val result = granted.toString()
        (webView?.context as? Activity)?.runOnUiThread {
            webView?.evaluateJavascript("onCameraPermissionResult($result);", null)
        }
    }

    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            Log.d("CustomBridge", "Permission result: $granted")
            permissionCallback?.invoke(granted)
        }
    }
}
