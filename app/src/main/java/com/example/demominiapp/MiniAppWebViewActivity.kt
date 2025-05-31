package com.example.demominiapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.demominiapp.sdk.*

class MiniAppWebViewActivity : AppCompatActivity() {

    private lateinit var messageBridge: CustomMiniAppMessageBridge

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val miniAppId = "com.example.miniapp1"
        val baseUrl = "http://10.0.2.2:3000"

        // Tạo bridge để xử lý gọi từ JS
        messageBridge = CustomMiniAppMessageBridge(this)

        messageBridge.onMessageReceived = { message ->
            // Gửi kết quả về MainActivity rồi finish
            val resultIntent = Intent().apply {
                putExtra("mini_app_message", message)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Tạo màn hình hiển thị mini app
        val display = MiniAppManager.create(
            context = this,
            miniAppId = miniAppId,
            baseUrl = baseUrl,
            miniAppMessageBridge = messageBridge
        )

        // Thiết lập WebView client để xử lý alert()
        val webView = display.getMiniAppView()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                AlertDialog.Builder(this@MiniAppWebViewActivity)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }
        }

        // Gắn WebView vào bridge để JS gọi ngược lại Android
        messageBridge.setWebView(webView)

        // Gắn WebView vào màn hình
        setContentView(webView)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        messageBridge.onRequestPermissionResult(requestCode, grantResults)
    }
}
