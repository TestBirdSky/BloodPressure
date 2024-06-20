package com.tele.core.tools

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpManager {
    private const val TAG = "app_net_log"
    private val http = OkHttpClient()

    fun startRequest(request: Request, event: String, max: Int, finish: () -> Unit) {
        if (max <= 0) {
            finish()
            return
        }
        try {
            Log.d(TAG, "start request $event")
            val response = http.newCall(request).execute()
            val msg = response.body?.string() ?: ""
            Log.e(TAG, "finish request $event, ${response.code}")
            if (response.code != 200) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(15000)
                    startRequest(request, event, max - 1, finish)
                }
            } else {
                if (event == "state") netState = msg
                saveState(event, event)
                finish()
            }
        } catch (e: Exception) {
            Log.e(TAG, "failed request $event, ${e.message}")
            CoroutineScope(Dispatchers.IO).launch {
                delay(15000)
                startRequest(request, event, max - 1, finish)
            }
        }
    }
}