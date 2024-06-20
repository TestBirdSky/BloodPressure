package com.blood.bloodthings.bigPage

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blood.bloodthings.R
import com.blood.bloodthings.databinding.ActivityFifthBinding

class FifthActivity : AppCompatActivity() {
    private lateinit var viewBinding:ActivityFifthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityFifthBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewBinding.backBtn.setOnClickListener { finish() }
        viewBinding.wevLoad.webViewClient = WebViewClient()
        viewBinding.wevLoad.loadUrl("https://www.baidu.com")
    }
}