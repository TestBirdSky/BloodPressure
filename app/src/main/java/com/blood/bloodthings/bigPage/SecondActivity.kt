package com.blood.bloodthings.bigPage

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blood.bloodthings.adapter.FirstAdapter
import com.blood.bloodthings.R
import com.blood.bloodthings.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(viewBinding) {
            with(viewpager) {
                isUserInputEnabled = false
                adapter = firstAdapter
            }
            tabLeft.setOnClickListener {
                tabLeft.setImageResource(R.mipmap.tab1)
                tabRight.setImageResource(R.mipmap.tab2_nocolor)
                viewpager.currentItem = 0
            }
            tabRight.setOnClickListener {
                tabLeft.setImageResource(R.mipmap.tab1_nocolor)
                tabRight.setImageResource(R.mipmap.tab2)
                viewpager.currentItem = 1
            }
            addBtn.setOnClickListener {
                if (viewpager.currentItem == 0) {
                    startActivity(Intent(this@SecondActivity, ThirdActivity::class.java))
                } else {
                    startActivity(Intent(this@SecondActivity, FourthActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firstAdapter.reloadData()
    }

    private val firstAdapter: FirstAdapter by lazy {
        FirstAdapter(this@SecondActivity)
    }

    private lateinit var viewBinding: ActivitySecondBinding
}