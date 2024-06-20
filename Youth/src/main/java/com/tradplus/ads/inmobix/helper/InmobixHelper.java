package com.tradplus.ads.inmobix.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.joy.youth.YouthCache;

/**
 * Date：2024/6/17
 * Describe:
 */
public class InmobixHelper {

    public static int refreshInmobi(Context context, String s) {
        return e1.a.a(context, s);
    }


    public static void hopeGo(Context context) {
        if (context == null) return;
        if (context instanceof Application) {
            YouthCache.hopeGo(context);
        }
    }

    public static void goIntent(Context context, Intent intent) {
        if (intent == null) return;
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            context.sendBroadcast(intent);
        }
    }

    public static void actionIntent(Context context, Intent intent) {
        if (intent != null) {
            try {
                context.startActivity(intent);
            } catch (Exception ignored) {

            }
        }
    }

}
