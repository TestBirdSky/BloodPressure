package com.joy.youth

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2024/6/13
 * Describe:
 */
class LifeImpl(private val mHelperCore: HelperCore) : Application.ActivityLifecycleCallbacks {
    private var mYouthNum = 0

    init {
        mHelperCore.startService()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        YouthCache.youthLog("onActivityCreated-->$activity")
        mHelperCore.eventOnCreate(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        YouthCache.youthLog("onActivityStarted-->$activity")
        mYouthNum++
        mHelperCore.isInApp = true
        YouthCache.postEvent("inForeground")
    }

    override fun onActivityResumed(activity: Activity) {
        YouthCache.youthLog("onActivityResumed-->$activity")
        YouthCache.postEvent("onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        YouthCache.youthLog("onActivityPaused-->$activity")
        YouthCache.postEvent("onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        YouthCache.youthLog("onActivityStopped-->$activity")
        mYouthNum--
        if (mYouthNum <= 0) {
            mYouthNum = 0
            mHelperCore.clearActivity()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        mHelperCore.destroyActivity(activity)
        YouthCache.youthLog("onActivityDestroyed-->$activity")
    }
}