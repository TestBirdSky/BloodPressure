package com.tele.core.observer

import com.applovin.sdk.app
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.remoteConfig
import com.tele.core.AppObserver
import com.tele.core.tools.adId
import com.tele.core.tools.edg
import com.tele.core.tools.g2
import com.tele.core.tools.skt

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

object FirebaseObserver : AppObserver {
    override fun observe() {
//        FirebaseApp.initializeApp(app) // TODO open
//        val firebase = Firebase.remoteConfig
//        firebase.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful) {
//                adId = firebase.getString("i_need_fa").ifBlank { "371300F6B233C9B15D1003E551464DDB" }
//                runCatching {
//                    val json = JSONObject(firebase.getString("i_need_cf"))
//                    g2 = json.getString("g2")
//                    skt = json.getString("skt")
//                    edg = json.getString("edg")
//                }
//                ConfigObserver.observe()
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                delay(30000)
//                observe()
//            }
//        }
    }
}