// MiniAppInterface.kt
package com.example.demominiapp.sdk.model

import android.content.Context
import com.example.demominiapp.sdk.MiniAppDisplay
import com.example.demominiapp.sdk.MiniAppMessageBridge

interface MiniAppInterface {
    fun create(
        context: Context,
        miniAppId: String,
        baseUrl: String,
        miniAppMessageBridge: MiniAppMessageBridge
    ): MiniAppDisplay
}
