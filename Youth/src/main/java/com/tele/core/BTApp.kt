package com.tele.core

import android.app.Application
import android.content.Context
import com.joy.youth.mApp
import com.tencent.mmkv.MMKV

/**
 * Date：2024/6/13
 * Describe:
 */
abstract class BTApp : Application() {

    override fun onCreate() {
        super.onCreate()
        mApp = this
        MMKV.initialize(this)
        runCatching {
            val clazz = Class.forName("com.tradplus.helper.TradplusHelper")
            clazz.getMethod("hopeGo", Context::class.java).invoke(null, this)
        }
    }
}