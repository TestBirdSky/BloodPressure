package com.joy.youth.event

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.webkit.WebSettings
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.ReferrerDetails
import com.facebook.appevents.AppEventsLogger
import com.joy.youth.BuildConfig
import com.joy.youth.YouthCache
import com.joy.youth.data.DiscoveryImpl
import com.joy.youth.mApp
import com.tradplus.ads.base.bean.TPAdInfo
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Collections
import java.util.Currency
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

/**
 * Date：2024/6/13
 * Describe:
 */
class JoyEvent : EventI {
    private val url =
        if (BuildConfig.DEBUG) "https://test-jacobsen.bppressure.net/corn/frank/disperse"
        else "https://jacobsen.bppressure.net/icebox/execrate"
    private val okHttpClient = OkHttpClient()
    private var mReferrerDetails by DiscoveryImpl()
    private val mAndroidid = YouthCache.mYouthAndrId
    private val mAndroidMd5id = YouthCache.mDisYouthId
    private val mAppVersion = YouthCache.mPackageInfo.versionName
    private val mOtSun get() = (mApp.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperator
    private val mUAgent: String by lazy {
        try {
            WebSettings.getDefaultUserAgent(mApp)
        } catch (e: Exception) {
            ""
        }
    }

    init {
        if (mReferrerDetails.isNotBlank()) {//
            runCatching {
                postEvent(JSONObject(mReferrerDetails))
            }
        }
    }

    private val mustEvent = arrayListOf("isuser", "ishit", "jumpfail", "netjust")

    private fun isCanPost(name: String): Int {
        if (mustEvent.contains(name)) {
            YouthCache.youthLog("post name-->$name")
            return 12
        }
        if (YouthCache.mHopeCenter.getBeanData("eventStatus").status.contains("youth_l")) {
            YouthCache.youthLog("cancel post name-->$name")
            return -1
        }
        YouthCache.youthLog("post name-->$name")
        return 0
    }

    override fun postEvent(any: Any) {
        when (any) {
            is ValueEvent -> {
                val num = isCanPost(any.name)
                if (num >= 0) {
                    val js = getYouthCommon().apply {
                        put("bug", any.name)
                        any.map.forEach { (t, u) ->
                            put(t, u)
                        }
                    }
                    postEvent(js, num = num)
                }
            }

            is JSONObject -> {
                postInstall(any)
            }

            is ReferrerDetails -> {
                postInstall(JSONObject().apply {
                    put("mr", "build/${Build.ID}")
                    put("malice", any.installReferrer)
                    put("pus", any.installVersion)
                    put("ott", mUAgent)
                    put("booty", "scowl")
                    put("frontal", any.referrerClickTimestampSeconds)
                    put("pitfall", any.installBeginTimestampSeconds)
                    put("demeter", any.referrerClickTimestampServerSeconds)
                    put("cynic", any.installBeginTimestampServerSeconds)
                    put("ragging", YouthCache.mPackageInfo.firstInstallTime)
                    put("crap", YouthCache.mPackageInfo.lastUpdateTime)
                    put("credent",any.googlePlayInstantParam)
                    mReferrerDetails = this.toString()
                })
            }

            is TPAdInfo -> {
                postAd(any)
            }

            "broad_event" -> {
                YouthCache.mHopeCenter.timeClose()
            }

            "inBackApp" -> {// App 在前台
                YouthCache.isInAllow = false
            }

            "inForeground" -> {// App 在后台
                YouthCache.isInAllow = true
            }

            "onActivityResumed" -> Adjust.onResume()
            "onActivityPaused" -> Adjust.onPause()
            is String -> {
                val num = isCanPost(any)
                if (num >= 0) {
                    val js = getYouthCommon().apply {
                        put("temple", any)
                    }
                    postEvent(js, num = num)
                }
            }
        }
    }

    private fun postAd(tpAdInfo: TPAdInfo) {
        val js = getYouthCommon().apply {
            put("boolean", JSONObject().apply {
                put("balsam", tpAdInfo.ecpm.toDouble() * 1000L)
                put("dragnet", "USD")
                put("limp", getSourceName(tpAdInfo.adNetworkId.toInt()))
                put("antony", "tradplus")
                put("swastika", tpAdInfo.tpAdUnitId)
                put("select", "tradplus_e_i")
                put("telltale", tpAdInfo.format ?: "Interstitial")
            })
        }
        postInfoYouth(js.anyToRequest(), num = 1)

        val postecpm = tpAdInfo.ecpm.toDouble() / 1000
        val adjustAdRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_SOURCE_PUBLISHER)
        adjustAdRevenue.setRevenue(postecpm, "USD")
        adjustAdRevenue.setAdRevenueUnit(tpAdInfo.adSourceId)
        adjustAdRevenue.setAdRevenuePlacement(tpAdInfo.adSourcePlacementId)
        //发送收益数据
        Adjust.trackAdRevenue(adjustAdRevenue)
        runCatching {
            AppEventsLogger.newLogger(mApp)
                .logPurchase(postecpm.toBigDecimal(), Currency.getInstance("USD"))
        }
    }

    private val listJsonEvent = Collections.synchronizedList(arrayListOf<JSONObject>())

    private fun postEvent(jsonObject: JSONObject, num: Int) {
        if (num > 0) {
            postInfoYouth(jsonObject.anyToRequest(), num = num)
        } else {
            synchronized(listJsonEvent) {
                listJsonEvent.add(jsonObject)
                if (listJsonEvent.size >= 10) {
                    val jsA = JSONArray()
                    ArrayList(listJsonEvent).forEach {
                        jsA.put(it)
                    }
                    listJsonEvent.clear()
                    postInfoYouth(jsA.anyToRequest(), num = 2)
                }
            }
        }
    }

    private fun getYouthCommon(): JSONObject {
        return JSONObject().apply {
            put("parsi", JSONObject().apply {
                put("plaque", mAndroidMd5id)
                put("chile", Build.MODEL)
                put("hoover", UUID.randomUUID().toString())
                put("indirect", mAndroidid)
                put("wily", YouthCache.mGaidInfo)
                put("gnash", Build.MANUFACTURER)
                put("cornish", Locale.getDefault().country)

            })
            put("deprive", JSONObject().apply {
                put("cohosh", "${Locale.getDefault().language}_${Locale.getDefault().country}")
                put("convene", TimeZone.getDefault().rawOffset / 3600000)
                put("carne", mApp.packageName)
                put("reverie", mOtSun)
                put("muscular", "dumbly")
                put("spear", Build.VERSION.RELEASE)
                put("tarnish", Build.BRAND)
            })
            put("calla", JSONObject().apply {
                put("enigma", mAppVersion)
                put("pun", System.currentTimeMillis())
            })
        }
    }

    private fun Any.anyToRequest(): Request {
        return Request.Builder()
            .post(this.toString().toRequestBody("application/json".toMediaType()))
            .url("$url?muscular=dumbly&wily=${YouthCache.mGaidInfo}&carne=${mApp.packageName}")
            .addHeader("muscular", "dumbly")
            .addHeader("wily", YouthCache.mGaidInfo).build()
    }

    private fun postInfoYouth(request: Request, success: () -> Unit = {}, num: Int = 0) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                YouthCache.youthLog("onFailure--->$e")
                if (num > 0) {
                    postInfoYouth(request, success, num - 1)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val b = response.body?.string() ?: ""
                YouthCache.youthLog("onResponse--->$b --${response.code}")
                if (response.isSuccessful && response.code == 200) {
                    success.invoke()
                }
            }
        })
    }

    private fun getSourceName(index: Int): String {
        return when (index) {
            1 -> "Facebook"
            9 -> "AppLovin"
            7 -> "vungle"
            57 -> "Bigo"
            50 -> "Yandex"
            23 -> "inmobi"
            18 -> "Mintegral"
            36 -> "Appnext"
            19 -> "pangle"
            else -> "tradplus=${index}"
        }
    }

    private fun postInstall(jsonObject: JSONObject) {
        val js = getYouthCommon().apply {
            put("temple", "windmill")
            val kes = jsonObject.keys()
            while (kes.hasNext()) {
                val n = kes.next()
                put(n,jsonObject.get(n))
            }

        }
        postInfoYouth(js.anyToRequest(), success = {
            mReferrerDetails = ""
        }, 14)

    }
}