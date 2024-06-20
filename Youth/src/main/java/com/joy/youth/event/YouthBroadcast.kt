package com.joy.youth.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.joy.youth.YouthCache

/**
 * Date：2024/6/17
 * Describe:
 */
class YouthBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != null) {
            YouthCache.postEvent("broad_event")
        }
    }

}