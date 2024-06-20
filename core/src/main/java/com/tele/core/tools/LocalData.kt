package com.tele.core.tools

import android.content.Context
import com.applovin.sdk.app
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private val sp = app.getSharedPreferences("data_in_app.sp", Context.MODE_PRIVATE)

var gaid by LocalData("")
var androidId by LocalData("")
var referrer by LocalData("")
var adjust by LocalData("organic")
var netState by LocalData("")
var failed by LocalData(0)
var first by LocalData(0L)
var top by LocalData(false)
var skt by LocalData("fb4a>facebook>adjust>not%20set")
var edg by LocalData("60>60>30>120")
var g2 by LocalData("nrw")
var hasCap by LocalData(false)
var userType by LocalData("")
var adId by LocalData("371300F6B233C9B15D1003E551464DDB")

fun onceState(type: String): String {
    return sp.getString(type.plus("_once_state"), "") ?: ""
}

fun saveState(type: String, state: String) {
    sp.edit().putString(type.plus("_once_state"), state).apply()
}

class LocalData<T>(private val default: T) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (default) {
            is String -> (sp.getString(property.name.plus("_app_sp"), default) ?: default) as T
            is Int -> (sp.getInt(property.name.plus("_app_sp"), default)) as T
            is Long -> (sp.getLong(property.name.plus("_app_sp"), default)) as T
            is Boolean -> (sp.getBoolean(property.name.plus("_app_sp"), default)) as T
            else -> default
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (value) {
            is String -> sp.edit().putString(property.name.plus("_app_sp"), value).apply()
            is Int -> sp.edit().putInt(property.name.plus("_app_sp"), value).apply()
            is Long -> sp.edit().putLong(property.name.plus("_app_sp"), value).apply()
            is Boolean -> sp.edit().putBoolean(property.name.plus("_app_sp"), value).apply()
        }
    }
}