package com.joy.youth.data

import android.content.Context
import android.util.Base64
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.joy.youth.BuildConfig
import com.joy.youth.IS_TEST
import com.joy.youth.TEST_C
import com.joy.youth.YouthCache
import com.joy.youth.YouthCache.mInfoAdjustNet
import org.json.JSONObject

/**
 * Date：2024/6/13
 * Describe:
 */
class FirebaseYouth : YouthData {
    private var lastFetchTime = 0L
    private var isRefresh = false
    var mRefreshUserList: RefreshUserList? = null

    // 广告ID 名字不能改变
    var mYouthAdId by DiscoveryImpl()

    override fun refreshData(context: Context) {
        if (System.currentTimeMillis() - lastFetchTime < 1000 * 60 * 50) return
        lastFetchTime = System.currentTimeMillis()
        runCatching {
            Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    runCatching {
                        val str = Firebase.remoteConfig.getString("joy_youth")
                        refreshCache(str)
                    }
                }
            }
        }
        if (isRefresh.not()) {
            isRefresh = true
            runCatching {
                val str = Firebase.remoteConfig.getString("joy_youth")
                refreshCache(str)
            }
            refreshAdjust(context)
            if (IS_TEST) {
                refreshCache(TEST_C)
            }
        }
    }

    private fun refreshCache(string: String) {
        runCatching {
            val str = String(Base64.decode(string, Base64.DEFAULT))
            JSONObject(str).apply {
                mYouthAdId = optString("youth_tt", "")
                status = optString("joy_status", "s123isk")
                val s = optString("youth_jo_tim", "30|-|30|-|2")
                if (s.contains("|-|")) {
                    refreshTime(s.split("|-|"))
                }
                val listStr = optString("ambition_us", "")
                if (listStr.isNotBlank()) {
                    val l = listStr.split("|")
                    listYouth.clear()
                    listYouth.addAll(l)
                    if (l.contains("not%20set")) {
                        listYouth.add("not set")
                    }
                }
            }
        }
    }

    private val listYouth = arrayListOf("not%20set", "not set", "adjust", "bytedance")


    var timeYouthC = 30_1000L
    var jobPT = 40_1000L // 显示间隔
    var joyTw = 120_1000L // 安装等待时间

    var status = ""


    var mShowBean = BeanData(status, jobPT, joyTw, timeYouthC)

    private var isCallback = false

    private fun refreshTime(list: List<String>) {
        runCatching {
            timeYouthC = list[0].toInt() * 1000L
            jobPT = list[1].toInt() * 1000L
            joyTw = list[2].toInt() * 1000L * 60
            mShowBean = BeanData(status, jobPT, joyTw, timeYouthC)
        }
    }

    private fun refreshAdjust(context: Context) {
        val environment =
            if (BuildConfig.DEBUG) AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION

        // todo modify adjust key
        val config = AdjustConfig(context, "ih2pm2dr3k74", environment)

        Adjust.addSessionCallbackParameter("customer_user_id", YouthCache.mDisYouthId)

        config.setOnAttributionChangedListener {
            isCallback = true
            YouthCache.youthLog("setOnAttributionChangedListener--->${it.network}")
            if (isMyNetJust().not()) {
                val network = it.network
                if (network.isNotBlank()) {
                    mInfoAdjustNet = network
                    if (isMyNetJust()) {
                        YouthCache.isAdjUser = true
                        YouthCache.postEvent("netjust")
                    }
                }
            }
        }

        Adjust.onCreate(config)
        YouthCache.isAdjUser = isMyNetJust()
    }

    private fun isMyNetJust(): Boolean {
        if (mInfoAdjustNet.isBlank()) return false
        return mInfoAdjustNet.contains("organic", true).not()
    }


}