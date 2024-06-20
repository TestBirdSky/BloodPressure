package com.joy.youth

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent


/**
 * Date：2024/6/17
 * Describe:
 */
abstract class BaseYouthJS : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        if (YouthCache.isInAllow.not()) {
            val pb = params?.extras
            val name = pb?.getString("H")
            if (name != null) {
                startInfo(this, name)
            }
        }
        return false
    }

    private fun startInfo(context: Context, n: String) { //这个函数可以放到其他文件减少关联
        try {
            val cn = ComponentName(context, n)
            val intent = Intent()
            intent.setClassName(context, cn.className)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartCommand(intent: Intent?, i: Int, i2: Int): Int {
        super.onStartCommand(intent, i, i2)
        return Service.START_STICKY
    }
}