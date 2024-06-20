package com.android.helper;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Date：2024/6/17
 * Describe:
 */
public class FcmNotification extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
