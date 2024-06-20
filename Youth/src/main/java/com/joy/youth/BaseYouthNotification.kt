package com.joy.youth

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Date：2024/6/17
 * Describe:
 */
abstract class BaseYouthNotification : Service() {
    private val mChannelId = "Notification"
    private val defBuild by lazy {
        NotificationCompat.Builder(this, mChannelId).setAutoCancel(false).setOngoing(true)
    }

    companion object {
        var isOnCreate = false

        fun startMe(context: Context, serClass: Class<*>) {
            runCatching {
                ContextCompat.startForegroundService(context, Intent(context, serClass))
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        isOnCreate = true
        showStartF()
    }

    protected fun showStartF() {
        startForeground(1089, getNotification())
    }

    private fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                mChannelId, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }
        return defBuild.setContentText("").setSmallIcon(R.drawable.youth_hope)
            .setOnlyAlertOnce(true).setContentTitle("")
            .setCustomContentView(RemoteViews(packageName, R.layout.hope_layout_ccv)).build()
    }

}