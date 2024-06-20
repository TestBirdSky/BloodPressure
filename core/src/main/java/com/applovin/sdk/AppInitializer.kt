package com.applovin.sdk

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.tele.core.observer.AdjustObserver
import com.tele.core.observer.AppInfoObserver
import com.tele.core.observer.ConfigObserver
import com.tele.core.observer.FirebaseObserver
import com.tele.core.observer.MainObserver
import com.tele.core.observer.ReferrerObserver
import com.tele.core.tools.ActivitiesCallback
import com.tele.core.tools.AppAdsManager
import com.tele.core.tools.first
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

lateinit var app: Application

class AppInitializer {
    private val observers = arrayListOf(ConfigObserver, FirebaseObserver, MainObserver, AppInfoObserver, AdjustObserver, AppAdsManager, ReferrerObserver)

    fun create(context: Context) {
        mainProcess(context) {
            app = context.applicationContext as Application
            app.registerActivityLifecycleCallbacks(ActivitiesCallback)
            if (first <= 0) first = System.currentTimeMillis()
            observers.forEach { it.observe() }
            notification()
        }
    }

    private fun notification() {
        val manager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(20)
        val running = services.any { "com.applovin.sdk.ApplovinService" == it.service.className }
        if (running.not() && ConfigObserver.notify && (ActivitiesCallback.foreground() || Build.VERSION.SDK_INT < Build.VERSION_CODES.S)) {
            val intent = Intent()
            intent.setClassName(app, "com.applovin.sdk.ApplovinService")
            ContextCompat.startForegroundService(app, intent)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                delay(30000)
                notification()
            }
        }
    }

    private fun mainProcess(context: Context, main: () -> Unit) {
        var processName = ""
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            val aManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            aManager.runningAppProcesses.forEach {
                if (it.pid == Process.myPid()) processName = it.processName
            }
        } else {
            processName = Application.getProcessName()
        }
        if (processName == context.packageName) {
            main()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }
}