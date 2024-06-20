package com.tele.core.observer

import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.tele.core.AppObserver
import com.applovin.sdk.app
import com.tele.core.tools.EventManager
import com.tele.core.tools.onceState
import com.tele.core.tools.referrer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ReferrerObserver : AppObserver {
    override fun observe() {
        if (onceState("install").isNotBlank()) return
        runCatching {
            val client = InstallReferrerClient.newBuilder(app).build()
            client.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(code: Int) {
                    runCatching {
                        if (code == InstallReferrerClient.InstallReferrerResponse.OK) {
                            val referrerString = client.installReferrer.installReferrer
                            if (!referrerString.isNullOrBlank()) {
                                referrer = referrerString
                                EventManager.install(client.installReferrer)
                            }
                            client.endConnection()
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() = Unit
            })
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(20000)
            observe()
        }
    }
}