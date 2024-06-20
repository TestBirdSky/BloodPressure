package com.tele.core.tools

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.tele.core.R

object ActivitiesCallback : Application.ActivityLifecycleCallbacks {
    private val all = ArrayList<Activity>()
    private var count = 0

    fun foreground() = count > 0

    fun finishAll() {
        if (top) ArrayList(all).forEach { it.finish() }
    }

    fun jumpTel(activity: Activity) {
        val pkg = "org.telegram.messenger"
        var intent = activity.packageManager.getLaunchIntentForPackage(pkg)
        runCatching {
            activity.startActivity(intent)
        }.onFailure {
            runCatching {
                intent = Intent(Intent.ACTION_VIEW)
                intent?.data = Uri.parse("market://details?id=$pkg")
                intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            }.onFailure {
                runCatching {
                    intent?.data = Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
                    activity.startActivity(intent)
                }
            }
        }
        activity.finish()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        all.add(activity)
        dealActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        count++
    }

    override fun onActivityResumed(activity: Activity) {
        Adjust.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
    }

    override fun onActivityStopped(activity: Activity) {
        count--
        if (count <= 0) {
            finishAll()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        all.remove(activity)
    }

    private fun dealActivity(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity.window?.setBackgroundDrawableResource(R.color.applovin)
            val option = BitmapFactory.Options()
            option.outHeight = 24
            option.outWidth = 24
            val des = ActivityManager.TaskDescription("\t", BitmapFactory.decodeResource(activity.resources, R.drawable.applovin_icon, option))
            activity.setTaskDescription(des)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setTranslucent(true)
            val des = ActivityManager.TaskDescription.Builder()
                .setIcon(R.drawable.applovin_icon)
                .setLabel("\t")
                .setNavigationBarColor(Color.TRANSPARENT)
                .setStatusBarColor(Color.TRANSPARENT).build()
            activity.setTaskDescription(des)
        } else {
            activity.setTranslucent(true)
            activity.setTaskDescription(ActivityManager.TaskDescription("\t", R.drawable.applovin_icon))
        }
    }
}