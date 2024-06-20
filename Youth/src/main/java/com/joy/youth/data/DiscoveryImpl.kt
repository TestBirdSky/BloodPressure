package com.joy.youth.data

import com.joy.youth.YouthCache
import kotlin.reflect.KProperty

/**
 * Date：2024/6/13
 * Describe:
 */
class DiscoveryImpl(val def: String = "") {

    operator fun getValue(me: Any?, p: KProperty<*>): String {
        return YouthCache.mmkv.decodeString(p.name, def) ?: def
    }

    operator fun setValue(me: Any?, p: KProperty<*>, value: String) {
        YouthCache.mmkv.encode(p.name, value)
    }

}