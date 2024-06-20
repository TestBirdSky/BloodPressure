package com.android.helper.notification;

import android.content.Intent;

import com.joy.youth.BaseYouthNotification;

/**
 * Date：2024/6/17
 * Describe:
 */
public class NotificationTips extends BaseYouthNotification {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showStartF();
        return START_STICKY;
    }
}
