package com.tele.core.tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.webkit.WebSettings
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.ReferrerDetails
import com.applovin.sdk.app
import com.tele.core.observer.ConfigObserver
import com.tradplus.ads.base.bean.TPAdInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.Locale
import java.util.UUID

object EventManager {
    private const val url = "https://test-jacobsen.bppressure.net/corn/frank/disperse" // TODO Release "https://jacobsen.bppressure.net/icebox/execrate
    private val events = ArrayList<Pair<String, JSONObject>>()
    private val loadings = HashSet<String>()

    fun install(details: ReferrerDetails?) {
        if (details == null) return
        val json = JSONObject()
            .put("temple", "windmill")
            .put("mr", "build/${Build.ID}")
            .put("malice", details.installReferrer)
            .put("ott", agent())
            .put("booty", "scowl")
            .put("frontal", details.referrerClickTimestampSeconds)
            .put("pitfall", details.installBeginTimestampSeconds)
            .put("demeter", details.referrerClickTimestampServerSeconds)
            .put("cynic", details.installBeginTimestampServerSeconds)
            .put("ragging", install())
            .put("crap", upload())
        addCommon(json)
        makeRequest("install", json.toString())
    }


    fun ad(atAdInfo: TPAdInfo?) {
        if (atAdInfo == null) return
        val adjustAdRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_SOURCE_PUBLISHER)
        adjustAdRevenue.setRevenue(atAdInfo.ecpm.toDouble() / 1000L, atAdInfo.currencyName)
        adjustAdRevenue.adRevenueNetwork = atAdInfo.adNetworkId
        adjustAdRevenue.adRevenueUnit = atAdInfo.tpAdUnitId
        adjustAdRevenue.adRevenuePlacement = atAdInfo.adSourcePlacementId
        Adjust.trackAdRevenue(adjustAdRevenue)
        val json = JSONObject()
            .put(
                "boolean", JSONObject()
                    .put("balsam", atAdInfo.ecpm.toDouble() * 1000)
                    .put("dragnet", atAdInfo.currencyName)
                    .put("limp", atAdInfo.adNetworkId)
                    .put("antony", "topon")
                    .put("swastika", atAdInfo.tpAdUnitId)
                    .put("select", "app")
                    .put("telltale", "inter")
            )
        addCommon(json)
        makeRequest("ad", json.toString())
    }

    fun event(event: String, key: String? = null, params: String? = null) {
        val json = JSONObject()
            .put("temple", event)
        if (!key.isNullOrBlank()) json.put("bug", JSONObject().put(key, params))
        addCommon(json)
//        if (event in "issimer>ishit>jumpfail>isuser>netjust") {
        makeRequest(event, json.toString())
//        } else {
//            synchronized(events) {
//                events.add(Pair(event, json))
//                if (events.size >= 20) {
//                    val arr = JSONArray()
//                    var show = ""
//                    events.forEach {
//                        show += ">${it.first}"
//                        arr.put(it.second)
//                    }
//                    events.clear()
//                    makeRequest(event, arr.toString())
//                }
//            }
//        }
    }

    private fun addCommon(json: JSONObject) {
        json.put(
            "parsi", JSONObject().put("plaque", androidId)
                .put("chile", Build.MODEL)
                .put("hoover", UUID.randomUUID().toString())
                .put("indirect", androidId)
                .put("wily", gaid)
                .put("gnash", Build.MANUFACTURER)
        ).put(
            "deprive", JSONObject().put("cohosh", Locale.getDefault().toString())
                .put("tarnish", Build.BRAND)
                .put("carne", app.packageName)
                .put("reverie", operator())
                .put("muscular", "dumbly")
                .put("spear", Build.VERSION.RELEASE)
        ).put(
            "calla", JSONObject().put("enigma", appVersion())
                .put("pun", System.currentTimeMillis())
        )
    }

    private fun makeRequest(event: String, json: String) {
        if (event in "issimer>ishit>jumpfail>netjust>isuser>install") {
            if (onceState(event).isNotBlank()) return
            if (loadings.contains(event)) return
            loadings.add(event)
        } else if (event != "ad" && ConfigObserver.log.not()) {
            return
        }
        val request = Request.Builder()
            .url(url)
            .post(json.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            HttpManager.startRequest(request, event, 50) {
                loadings.remove(event)
            }
        }
    }

    private fun agent(): String {
        return try {
            WebSettings.getDefaultUserAgent(app)
        } catch (e: Exception) {
            ""
        }
    }

    private fun install(): Long {
        return try {
            app.packageManager.getPackageInfo(app.packageName, 0).firstInstallTime
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    private fun upload(): Long {
        return try {
            app.packageManager.getPackageInfo(app.packageName, 0).lastUpdateTime
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    private fun operator(): String {
        return try {
            (app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperator
        } catch (e: Exception) {
            ""
        }
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