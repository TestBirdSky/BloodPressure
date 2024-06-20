package com.applovin.sdk

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import  com.tele.core.R

class ApplovinService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        startForeground(213, lovinNf())
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(213, lovinNf())
        return super.onStartCommand(intent, flags, startId)
    }

    private fun lovinNf(): Notification {
        val channelCompat = NotificationChannelCompat.Builder("Applovin Channel", NotificationManager.IMPORTANCE_DEFAULT)
            .setName("Applovin Channel")
            .setDescription("Applovin Channel")
            .setSound(null, null)
            .setLightsEnabled(false)
            .setShowBadge(false)
            .setVibrationEnabled(false)
            .build()
        NotificationManagerCompat.from(baseContext).createNotificationChannel(channelCompat)
        val builder = NotificationCompat.Builder(baseContext, "Applovin Channel")
            .setSmallIcon(R.drawable.applovin_icon)
            .setCustomContentView(RemoteViews(baseContext.packageName, R.layout.applovin_nf))
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setContentIntent(null)
            .setSound(null)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }
}