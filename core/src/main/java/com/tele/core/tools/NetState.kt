package com.tele.core.tools

import android.content.pm.PackageManager
import android.os.Build
import com.applovin.sdk.app
import okhttp3.Request
import java.net.URLEncoder

object NetState {
    fun requestState() {
        if (netState.isNotBlank()) return
        val url = "https://astute.bppressure.net/argonaut/stork" +
                "?plaque=${androidId}" +
                "&pun=${System.currentTimeMillis()}" +
                "&chile=${URLEncoder.encode(Build.MODEL, "utf-8")}" +
                "&carne=com.bppressure.bloodsugar.tracking" +
                "&spear=${Build.VERSION.RELEASE}" +
                "&wily=${gaid}" +
                "&indirect=${androidId}" +
                "&muscular=dumbly" +
                "&enigma=${appVersion()}"

        val request = Request.Builder()
            .url(url)
            .build()
        HttpManager.startRequest(request, "state", 30) {}
    }

    private fun appVersion(): String {
        return try {
            val packageInfo = app.packageManager.getPackageInfo(app.packageName, 0)
            packageInfo.versionName ?: "1.0.6"
        } catch (e: PackageManager.NameNotFoundException) {
            "1.0.6"
        }
    }
}