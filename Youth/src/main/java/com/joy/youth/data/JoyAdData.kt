package com.joy.youth.data

import android.app.Activity
import android.content.Context
import com.joy.youth.YouthCache
import com.joy.youth.event.ValueEvent
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.TradPlusSdk
import com.tradplus.ads.open.interstitial.InterstitialAdListener
import com.tradplus.ads.open.interstitial.TPInterstitial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Date：2024/6/13
 * Describe:
 */
class JoyAdData(val context: Context) : YouthData, InterstitialAdListener {
    private var isInitSuccess = false

    private var mTpInterstitial: TPInterstitial? = null

    // 广告ID 名字毕必须和存储的一样
    private var mYouthAdId by DiscoveryImpl()
    private var lastJoyTime = 0L

    // 存储时间
    private var lastJoyedTime = 0L
    private var isLoadJoy = false

    init {
        // 初始化广告
        TradPlusSdk.setTradPlusInitListener {
            // 初始化成功，建议在该回调后 发起广告请求
            isInitSuccess = true
            refreshData(context)
        }

        TradPlusSdk.initSdk(context, "480B8D4FC0B706567FC8AF4B129DD2AE")
    }

    override fun refreshData(context: Context) {
        if (isInitSuccess.not()) return
        if (mYouthAdId.isBlank()) return
        CoroutineScope(Dispatchers.Main).launch {
            if (isLoadJoy) {
                if (System.currentTimeMillis() - lastJoyTime < 60_1000) {
                    return@launch
                }
            }
            if (isReady()) {
                YouthCache.youthLog("have ad")
                return@launch
            }
            isLoadJoy = true
            lastJoyedTime = System.currentTimeMillis()
            lastJoyTime = System.currentTimeMillis()
            if (mTpInterstitial == null) {
                mTpInterstitial = TPInterstitial(context, mYouthAdId)
            }
            mTpInterstitial?.setAdListener(this@JoyAdData)
            mTpInterstitial?.loadAd()
            YouthCache.postEvent("reqprogress")
        }
    }

    fun isReady(): Boolean {
        val ad = mTpInterstitial ?: return false
        if (System.currentTimeMillis() - lastJoyedTime > 1000 * 60 * 55) {
            mTpInterstitial = null
            return false
        }
        return ad.isReady
    }

    private var closeInfo: (() -> Unit)? = null

    fun showJoy(activity: Activity): String {
        val ad = mTpInterstitial ?: return "ad null"
        if (isReady()) {
            closeInfo = {
                activity.finishAndRemoveTask()
            }
            ad.showAd(activity, "")
            return "joy success"
        } else {
            return "ad not ready"
        }
    }

    // tradplus listener start
    override fun onAdLoaded(p0: TPAdInfo?) {
        isLoadJoy = false
        lastJoyedTime = System.currentTimeMillis()
        YouthCache.postEvent("getprogress")
        YouthCache.mHopeCenter.loadSuccessEvent?.invoke()
        YouthCache.mHopeCenter.loadSuccessEvent = null
    }

    override fun onAdFailed(p0: TPAdError?) {
        YouthCache.postEvent(
            ValueEvent(
                "showfailer", mapOf("string" to "${p0?.errorCode}_${p0?.errorMsg}")
            )
        )
        if (isLoadJoy) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(14009)
                isLoadJoy = false
                refreshData(context)
            }
        }
    }

    override fun onAdImpression(p0: TPAdInfo?) {
        YouthCache.postEvent("showsuccess")
        p0?.let {
            YouthCache.postEvent(it)
        }
        refreshData(context)
    }

    override fun onAdClicked(p0: TPAdInfo?) {
    }

    override fun onAdClosed(p0: TPAdInfo?) {
        closeInfo?.invoke()
        closeInfo = null
    }

    override fun onAdVideoError(p0: TPAdInfo?, p1: TPAdError?) {
        closeInfo?.invoke()
        closeInfo = null
    }

    override fun onAdVideoStart(p0: TPAdInfo?) {
    }

    override fun onAdVideoEnd(p0: TPAdInfo?) {
    }
    // tradplus listener end


}