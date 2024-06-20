package com.paktanidev.guideDINOS

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blood.bloodthings.R
import com.blood.bloodthings.bigPage.SecondActivity
import com.blood.bloodthings.databinding.ActivityFirstBinding
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val paint = viewBinding.appName.paint
        val width = paint.measureText(viewBinding.appName.getText().toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, viewBinding.appName.textSize, intArrayOf(
                Color.parseColor("#5AE077"),
                Color.parseColor("#408C96"),
            ), null, Shader.TileMode.CLAMP
        )

        viewBinding.appName.paint.setShader(textShader)
        viewBinding.appName.invalidate()


        val timer = Timer()
        val startTime = System.currentTimeMillis()

        val task: TimerTask = object : TimerTask() {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                if (elapsedTime < 2000) {
                    runOnUiThread {
                        with(viewBinding) {
                            val allWidth = percentBg.width
                            val layout = percentNow.layoutParams
                            layout.width = (1.0 * elapsedTime / 2000 * allWidth).toInt()
                            percentNow.layoutParams = layout
                        }
                    }
                } else {
                    runOnUiThread {
                        startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                        finish()
                    }
                    timer.cancel()
                }
            }
        }
        timer.schedule(task, 0, 100)
    }

    private lateinit var viewBinding: ActivityFirstBinding
}