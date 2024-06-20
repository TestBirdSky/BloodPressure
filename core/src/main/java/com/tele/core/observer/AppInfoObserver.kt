package com.tele.core.observer

import android.content.Context
import android.telephony.TelephonyManager
import com.tele.core.AppObserver
import com.applovin.sdk.app
import com.tele.core.tools.EventManager
import com.tele.core.tools.NetState
import com.tele.core.tools.adjust
import com.tele.core.tools.hasCap
import com.tele.core.tools.netState
import com.tele.core.tools.referrer
import com.tele.core.tools.skt
import com.tele.core.tools.top
import com.tele.core.tools.userType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

object AppInfoObserver : AppObserver {
    override fun observe() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            NetState.requestState()
            while (top.not()) {
                delay(2000)
                if (hasCap.not()) {
                    hasCap = if (ConfigObserver.card) {
                        val manager = app.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
                        manager?.simState != TelephonyManager.SIM_STATE_ABSENT
                    } else {
                        true
                    }
                }
                if (hasCap) {
                    EventManager.event("issimer")
                    if (userType.isBlank()) {
//                        val str = referrer // TODO
                        val str = "adjust"
                        val tps = skt.split(">")
                        userType = tps.find { tp -> str.contains(tp, true) } ?: ""
                        if (userType.isBlank() && adjust.contains("organic", true).not()) {
                            userType = "adjust"
                        } else if (userType !in "adjustbytedance") {
                            val country = Locale.getDefault().country.lowercase()
                            if (country in "in>sg>us>ke") {
                                userType = ""
                            }
                        }
                    }
                    if (userType.isNotBlank()) {
                        EventManager.event("isuser", "getstring", userType)
                        if (netState == "teakwood") {
                            EventManager.event("ishit")
                            top = true
                        }
                    }
                }
            }
        }
    }
}