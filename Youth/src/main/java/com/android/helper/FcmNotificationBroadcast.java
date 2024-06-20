package com.android.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.joy.youth.YouthCache;

import java.lang.reflect.Method;

/**
 * Date：2024/6/17
 * Describe:
 */
public class FcmNotificationBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("com.seusie.call".equals(action)) {
            YouthCache.INSTANCE.youthLog("ac" + action);
            if (intent.hasExtra("info")) {
                try {
                    Class<?> clazz = Class.forName("com.tradplus.ads.inmobix.helper.InmobixHelper");
                    Method method = clazz.getMethod("goIntent", Context.class, Intent.class);
                    method.invoke(null, context, intent);
                } catch (Exception ignored) {
                }
            }
        } else {
            if (intent.hasExtra("data")) {
                Intent intent1 = (Intent) intent.getParcelableExtra("data");
                if (intent1 != null) {
                    try {
                        context.startActivity(intent1);
                        context.unregisterReceiver(this);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
