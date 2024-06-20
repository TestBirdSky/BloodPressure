package com.tele.core.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tele.core.observer.ConfigObserver
import com.tele.core.observer.MainObserver

var lastCharge = 0L

class AppReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (System.currentTimeMillis() - lastCharge < 2000) return
        lastCharge = System.currentTimeMillis()
        if (failed < ConfigObserver.failed) {
            EventManager.event("time_charge")
            MainObserver.nextStep()
        } else {
            EventManager.event("jumpfail")
        }
    }
}