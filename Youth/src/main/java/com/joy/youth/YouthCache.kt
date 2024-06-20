package com.joy.youth

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.provider.Settings
import android.util.Log
import com.joy.youth.data.DiscoveryImpl
import com.joy.youth.event.JoyEvent
import com.tencent.mmkv.MMKV
import java.math.BigInteger
import java.security.MessageDigest
import java.util.UUID

/**
 * Date：2024/6/13
 * Describe:
 */
object YouthCache {
    lateinit var mHopeCenter: HopeCenter
    var mInfoAdjustNet by DiscoveryImpl()
    var isInAllow = false

    var isAdjUser = false

    val mmkv by lazy { MMKV.defaultMMKV() }

    // android id
    var mYouthAndrId by DiscoveryImpl("")

    // distinctid
    var mDisYouthId = ""

    var mGaidInfo by DiscoveryImpl("")

    var mInstallTime = 0L

    lateinit var mPackageInfo: PackageInfo

    private fun initCommonData(context: Context) {
        mPackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        if (mYouthAndrId.isBlank()) {
            mYouthAndrId = Settings.System.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            ).ifBlank {
                UUID.randomUUID().toString()
            }
        }
        mDisYouthId = mYouthAndrId.toMdInfo()
        mInstallTime = mPackageInfo.firstInstallTime
    }

    @JvmStatic
    fun hopeGo(context: Context) {
        mHopeCenter = if (context is Application) {
            HopeCenter(context)
        } else {
            HopeCenter()
        }
        if (mHopeCenter.isYouth) {
            initCommonData(context)
        }
        mHopeCenter.refreshData(context)
    }

    private fun String.toMdInfo(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray())
        val bigInt = BigInteger(1, digest)
        var md5 = bigInt.toString(16)
        // 补齐前导0以确保MD5字符串长度为32
        while (md5.length < 32) {
            md5 = "0$md5"
        }
        return md5
    }

    fun youthLog(msg: String) {
        if (IS_TEST) {
            Log.e("YOUTH_LOG", msg)
        }
    }

    private val jobEvent by lazy { JoyEvent() }

    fun postEvent(any: Any) {
        jobEvent.postEvent(any)
    }

    //注意:第2个参数传字符串::字符串包含"ch"隐藏图标,包含"ju"恢复隐藏.包含"in"外弹(外弹在子线程调用).(保证i参数不容易关联)
    fun infoStr(context: Context, string: String) {
        runCatching {
            val clazz: Class<*> = Class.forName("com.tradplus.helper.TradplusHelper")
            clazz.getMethod("refreshInmobi", Context::class.java, String::class.java)
                .invoke(null, context, string)
        }.onFailure {
            it.printStackTrace()
        }
    }

}