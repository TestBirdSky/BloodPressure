package com.tele.core.observer

import android.annotation.SuppressLint
import android.provider.Settings
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.applovin.sdk.app
import com.tele.core.AppObserver
import com.tele.core.tools.EventManager
import com.tele.core.tools.adjust
import com.tele.core.tools.androidId
import java.util.UUID

object AdjustObserver : AppObserver {
    override fun observe() {
        updateAndroidId()
        val config = AdjustConfig(app, "xxxx", AdjustConfig.ENVIRONMENT_SANDBOX) // TODO release
        config.setOnAttributionChangedListener {
            val network = it.network.lowercase()
            if (network.isNotBlank()) {
                val local = adjust
                if (local.contains("organic") && !network.contains("organic")) {
                    adjust = network
                    EventManager.event("netjust")
                }
            }
        }
        Adjust.addSessionCallbackParameter("customer_user_id", androidId)
        Adjust.onCreate(config)
    }

    @SuppressLint("HardwareIds")
    private fun updateAndroidId() {
        var id = androidId
        if (id.isBlank()) {
            id = Settings.Secure.getString(app.contentResolver, Settings.Secure.ANDROID_ID)
            if (id.isNullOrBlank() || id in arrayOf("9774d56d682e549c", "0000000000000000")) {
                id = UUID.randomUUID().toString()
            }
            androidId = id
        }
    }
}