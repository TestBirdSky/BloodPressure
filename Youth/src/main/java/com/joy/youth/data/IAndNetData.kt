package com.joy.youth.data

import android.content.Context
import android.os.Build
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.joy.youth.IS_TEST
import com.joy.youth.YouthCache
import com.joy.youth.event.ValueEvent
import com.joy.youth.mApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Locale

/**
 * Date：2024/6/13
 * Describe:
 */
class IAndNetData : YouthData, Callback, RefreshUserList {
    private val mOkHttpClient get() = OkHttpClient()
    private var mReferrerInfo by DiscoveryImpl()
    private var mNetCloak by DiscoveryImpl()

    private val isAreaCountry: Boolean =
        arrayListOf("SG", "US", "IN", "KE").find { it == Locale.getDefault().country } != null

    fun isNetGo(): Boolean {
        return mNetCloak == "teakwood"
    }

    fun isNetLi(): Boolean {
        return mNetCloak == "skiddy"
    }

    suspend fun userCheck(): String {
        while (true) {
            if (isUYouth()) {
                return "u_go"
            }
            if (isNetLi()) {
                return "u_cancel"
            }
            delay(5601)
        }
    }

    private fun isUYouth(): Boolean {
        fun postUser(string: String) {
            YouthCache.postEvent(ValueEvent("isuser", mapOf("getstring" to string)))
        }
        ArrayList(listYouth).forEach {
            if (isAreaCountry.not() || it == "adjust" || it == "bytedance") {
                if (mReferrerInfo.contains(it)) {
                    postUser(it)
                    return true
                }
            }
        }
        if (YouthCache.isAdjUser) {
            postUser(YouthCache.mInfoAdjustNet)
            return true
        } else return false
    }

    private val listYouth = arrayListOf("not%20set", "not set", "adjust", "bytedance")

    override fun refreshData(context: Context) {
        var length = ""
        if (mReferrerInfo.isBlank()) {
            loadInstall(context)
            length += ('a'..'z').random()
        }
        if (mNetCloak.isBlank()) {
            length += ('a'..'h').random()
            loadNet(context)
        }
        if (length.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(14001)
                refreshData(context)
            }
        }
    }

    private fun loadInstall(context: Context) {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(p0: Int) {
                runCatching {
                    if (p0 == InstallReferrerClient.InstallReferrerResponse.OK) {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        mReferrerInfo = response.installReferrer
                        //todo delete
                        if (IS_TEST) {
                            YouthCache.youthLog("mGoogleReferStr-->${mReferrerInfo}")
                            mReferrerInfo += "adjust"
                        }
                        YouthCache.postEvent(response)
                        referrerClient.endConnection()
                    } else {
                        referrerClient.endConnection()
                    }
                }.onFailure {
                    referrerClient.endConnection()
                }
            }

            override fun onInstallReferrerServiceDisconnected() = Unit
        })
    }

    private fun getCUrl(): String {
        val strBuilder = StringBuilder()
        val packageN = if (IS_TEST) "com.bppressure.bloodsugar.tracking" else mApp.packageName
        strBuilder.append("carne=$packageN&muscular=dumbly")
        strBuilder.append("&enigma=${YouthCache.mPackageInfo.versionName}&chile=${Build.MODEL}&spear=${Build.VERSION.RELEASE}")
        strBuilder.append("&plaque=${YouthCache.mDisYouthId}&pun=${System.currentTimeMillis()}")
        strBuilder.append("&wily=${YouthCache.mGaidInfo}&indirect=${YouthCache.mYouthAndrId}")
        return "https://astute.bppressure.net/argonaut/stork?$strBuilder&tarnish=${Build.BRAND}"
    }

    private fun loadNet(context: Context) {
        val reques = Request.Builder().url(getCUrl()).get().build()
        mOkHttpClient.newCall(reques).enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        YouthCache.youthLog("onFailure-->$e")
    }

    override fun onResponse(call: Call, response: Response) {
        val body = response.body?.string() ?: ""
        YouthCache.youthLog("--->$body")
        if (response.isSuccessful && response.code == 200) {
            mNetCloak = body
        }
    }

    override fun list(mList: List<String>) {
        listYouth.clear()
        listYouth.addAll(mList)
    }
}