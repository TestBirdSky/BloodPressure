package com.joy.youth

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.joy.youth.data.BeanData
import com.joy.youth.data.FirebaseYouth
import com.joy.youth.data.IAndNetData
import com.joy.youth.data.JoyAdData
import com.joy.youth.data.YouthData
import com.joy.youth.event.ValueEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2024/6/13
 * Describe:
 */
class HopeCenter(val context: Application = mApp) : YouthData {
    private lateinit var mFirebaseYouth: FirebaseYouth
    private lateinit var joyAdData: JoyAdData
    private lateinit var mIAndNetData: IAndNetData
    private lateinit var mYouthIdentity: YouthIdentity
    val mHelperCore by lazy { HelperCore(context) }
    var isYouth = false

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName: String = Application.getProcessName()
            if (!context.packageName.equals(processName)) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
        isYouth = context.packageName == context.getCurProName()
    }

    private fun Context.getCurProName(): String {
        runCatching {
            val am = getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses ?: return ""
            for (info in runningApps) {
                when (info.pid) {
                    android.os.Process.myPid() -> return info.processName
                }
            }
        }
        return ""
    }

    override fun refreshData(context: Context) {
        if (isYouth.not()) return
        if (YouthCache.mGaidInfo.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    YouthCache.mGaidInfo = AdvertisingIdClient.getAdvertisingIdInfo(mApp).id ?: ""
                }
            }
        }
        joyAdData = JoyAdData(context)
        mFirebaseYouth = FirebaseYouth()
        mIAndNetData = IAndNetData()

        mFirebaseYouth.mRefreshUserList = mIAndNetData

        mFirebaseYouth.refreshData(context)
        mIAndNetData.refreshData(context)

        startTimer()
        if (context is Application) {
            context.registerActivityLifecycleCallbacks(LifeImpl(mHelperCore))
        }

        mYouthIdentity = YouthIdentity(context, mIAndNetData)
        mYouthIdentity.youthYearCheck()
    }


    private fun startTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            while (true) {
                joyAdData.refreshData(context)
                delay(mFirebaseYouth.timeYouthC)
                mFirebaseYouth.refreshData(context)
            }
        }
    }


    fun getBeanData(type: String): BeanData {
        return when (type) {
            "show" -> mFirebaseYouth.mShowBean
            "wait" -> BeanData(tWait = mFirebaseYouth.joyTw)
            "eventStatus" -> BeanData(status = mFirebaseYouth.status, mFirebaseYouth.jobPT)
            else -> {
                BeanData()
            }
        }
    }


    fun timeClose() {
        if (mYouthIdentity.is18Years()) {
            YouthCache.postEvent("time_charge")
            mYouthIdentity.goToSch()
        }
    }

    fun isAllowChild(): Boolean {
        return mYouthIdentity.is18Years()
    }

    fun isReady(): Boolean {
        return joyAdData.isReady()
    }

    fun showJoy(activity: Activity) {
        YouthCache.postEvent("startup")
        val launcher = if (activity is AppCompatActivity) {
            activity.lifecycleScope
        } else {
            CoroutineScope(Dispatchers.Main)
        }
        launcher.launch {
            YouthCache.youthLog("start111")
            waitResume(activity)
            YouthCache.youthLog("start222")
            val result = joyAdData.showJoy(activity)
            if (result == "joy success") {

            } else {
                activity.finishAndRemoveTask()
                YouthCache.postEvent(ValueEvent("showfailer", mapOf("string" to result)))
            }
        }
    }

    private suspend fun waitResume(activity: Activity) {
        val startTime = System.currentTimeMillis()
        if (activity is AppCompatActivity) {
            while (System.currentTimeMillis() - startTime < 500) {
                delay(30)
                if (activity.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    break
                }
            }
        } else {
            delay(105)
        }
    }

    fun setLoadSuccess(event: () -> Unit) {
        loadSuccessEvent = event
        joyAdData.refreshData(context)
    }

    var loadSuccessEvent: (() -> Unit)? = null
}