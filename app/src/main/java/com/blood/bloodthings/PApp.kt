package com.blood.bloodthings

import android.content.Context
import android.content.SharedPreferences
import com.tele.core.BTApp
import io.objectbox.Box
import io.objectbox.BoxStore

class PApp : BTApp() {
    companion object {
        lateinit var boxStore: BoxStore
            private set

        lateinit var PressDao: Box<PressureData>
        lateinit var SweetDao: Box<SweetData>


        lateinit var spObject: SharedPreferences
        fun saveCheck(content: String) {
            spObject.edit().putString("checkResult", content).apply()
        }
    }

    override fun onCreate() {
        super.onCreate()
        boxStore = MyObjectBox.builder().androidContext(this).build()

        PressDao = boxStore.boxFor(PressureData::class.java)
        SweetDao = boxStore.boxFor(SweetData::class.java)


        spObject = applicationContext.getSharedPreferences("spObject", Context.MODE_PRIVATE)
        spId = spObject.getString("spId", "") ?: ""
    }
}