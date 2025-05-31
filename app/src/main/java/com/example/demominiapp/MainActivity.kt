package com.example.demominiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.demominiapp.ui.theme.DemoMiniAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var messageTextView: TextView

    companion object {
        var instance: MainActivity? = null

        fun updateMessageFromMiniApp(message: String) {
            instance?.runOnUiThread {
                instance?.messageTextView?.text = "Mini App nói: $message"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val button = Button(this).apply {
            text = "Mở Mini App"
            setOnClickListener {
                val intent = Intent(this@MainActivity, MiniAppWebViewActivity::class.java)
                startActivity(intent)
            }
        }

        messageTextView = TextView(this).apply {
            text = "Chưa có message từ Mini App"
        }

        layout.addView(button)
        layout.addView(messageTextView)

        setContentView(layout)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
