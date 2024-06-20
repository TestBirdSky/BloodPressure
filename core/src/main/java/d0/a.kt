package d0

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ComponentName
import android.content.Intent

class a : JobService() {
    @SuppressLint("NewApi")
    override fun onStartJob(params: JobParameters): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        var main = false
        am.runningAppProcesses?.forEach {
            if (it.processName == applicationInfo.processName && it.importance == 100) {
                main = true
            }
        }
        if (!main) {
            val pb = params.extras
            runCatching {
                val cn = ComponentName(this, pb.getString("z")!!)
                val intent = Intent()
                intent.setClassName(this, cn.className)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }

    override fun onStartCommand(intent: Intent, i: Int, i2: Int): Int {
        super.onStartCommand(intent, i, i2)
        return START_STICKY
    }
}
