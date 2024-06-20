package com.tele.core.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.applovin.sdk.app
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.tele.core.AppObserver
import com.tele.core.observer.ConfigObserver
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.TradPlusSdk
import com.tradplus.ads.open.interstitial.InterstitialAdListener
import com.tradplus.ads.open.interstitial.TPInterstitial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("StaticFieldLeak")
object AppAdsManager : AppObserver, InterstitialAdListener {
    private val cache by lazy { TPInterstitial(app, adId).apply { setAdListener(this@AppAdsManager) } }
    private var time = 0L
    private var job: Job? = null
    private var start = 0L
    private var loading = false

    fun hasCache(): Boolean {
        if (cache.isReady.not()) return false
        if (System.currentTimeMillis() - time < 55 * 60 * 1000L) {
            return true
        } else {
            cache.onDestroy()
            cache.setAdListener(this)
            load()
            return false
        }
    }

    fun load() {
        if (loading && System.currentTimeMillis() - start < 90 * 1000L) return
        loading = true
        start = System.currentTimeMillis()
        time = System.currentTimeMillis()
        EventManager.event("reqprogress")
        cache.loadAd()
    }

    fun show(activity: Activity) {
        EventManager.event("startup")
        failed = 0
        if (cache.isReady.not()) {
            EventManager.event("showfailer", "string", "not ready")
            load()
            ActivitiesCallback.finishAll()
        } else {
            jumpHome(activity)
            job?.cancel()
            job = CoroutineScope(Dispatchers.IO).launch {
                delay(12000)
                ActivitiesCallback.finishAll()
            }
            cache.showAd(activity, null)
        }
    }

    private fun loop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                load()
                delay(ConfigObserver.detect)
            }
        }
    }

    private fun jumpHome(activity: Activity) {
        if (Build.VERSION.SDK_INT <= 30) return
        if (dateToStamp() < 1688140800) return
        runCatching {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT < 29) {
                PendingIntent.getActivity(activity, 130, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE).send()
            } else {
                activity.startActivity(intent)
            }
        }
    }

    private fun dateToStamp(): Long {
        val date = Build.VERSION.SECURITY_PATCH
        var time = -1L
        runCatching {
            val df = if (date.contains(",")) {
                java.text.SimpleDateFormat("yyyy,MM,dd", Locale.getDefault())
            } else {
                java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            }
            val dt = df.parse(date)
            if (dt != null) time = dt.time / 1000
        }
        return time
    }

    override fun observe() {
        TradPlusSdk.initSdk(app, "4776AC11055B82EA736E947B03C368C6")
        googleId()
        loop()
    }

    private fun googleId() {
        var id = gaid
        if (id.isBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                id = try {
                    AdvertisingIdClient.getAdvertisingIdInfo(app).id ?: ""
                } catch (e: Exception) {
                    ""
                }
                gaid = id
            }
        }
    }

    override fun onAdLoaded(p0: TPAdInfo?) {
        EventManager.event("getprogress")
        time = System.currentTimeMillis()
        loading = false
    }

    override fun onAdFailed(p0: TPAdError?) {
        EventManager.event("showfailer", "string", "load failed, ${p0?.errorMsg}")
        loading = false
    }

    override fun onAdImpression(p0: TPAdInfo?) {
        job?.cancel()
        EventManager.ad(p0)
        EventManager.event("showsuccess")
        ConfigObserver.last = System.currentTimeMillis()
        load()
    }

    override fun onAdClicked(p0: TPAdInfo?) {
    }

    override fun onAdClosed(p0: TPAdInfo?) {
        ActivitiesCallback.finishAll()
    }

    override fun onAdVideoError(p0: TPAdInfo?, p1: TPAdError?) {
    }

    override fun onAdVideoStart(p0: TPAdInfo?) {
    }

    override fun onAdVideoEnd(p0: TPAdInfo?) {
    }
}