package com.tele.core

import android.app.Application
import android.content.Context
import com.adjust.sdk.Reflection

abstract class BTApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val ins = Reflection.createDefaultInstance("com.applovin.sdk.AppInitializer")
        Reflection.invokeInstanceMethod(ins, "create", arrayOf(Context::class.java), this)
    }
}