package com.blood.bloodthings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blood.bloodthings.R
import com.blood.bloodthings.SweetTypeVh
import com.blood.bloodthings.databinding.ItemSweetTypeBinding

class FifthAdapter : RecyclerView.Adapter<SweetTypeVh>() {

    var data = arrayListOf("1", "2", "3", "4")
    var currentType = "2"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SweetTypeVh {
        val view = ItemSweetTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SweetTypeVh(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SweetTypeVh, position: Int) {
        with(holder.bView) {
            when (data[position]) {
                "1" -> {
                    sweetIc.setImageResource(R.mipmap.sweet_blue)
                    viewIndicator.setBackgroundResource(R.drawable.sweet_indicator_1)
                }

                "2" -> {
                    sweetIc.setImageResource(R.mipmap.sweet_green)
                    viewIndicator.setBackgroundResource(R.drawable.sweet_indicator_2)
                }

                "3" -> {
                    sweetIc.setImageResource(R.mipmap.sweet_yellow)
                    viewIndicator.setBackgroundResource(R.drawable.sweet_indicator_3)
                }

                "4" -> {
                    sweetIc.setImageResource(R.mipmap.sweet_red)
                    viewIndicator.setBackgroundResource(R.drawable.sweet_indicator_4)
                }
            }
            viewIndicator.visibility =
                if (data[position] == currentType) View.VISIBLE else View.INVISIBLE
        }
    }
}