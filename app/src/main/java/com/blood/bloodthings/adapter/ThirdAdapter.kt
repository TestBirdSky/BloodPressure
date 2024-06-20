package com.blood.bloodthings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blood.bloodthings.PressureData
import com.blood.bloodthings.R
import com.blood.bloodthings.SweetData
import com.blood.bloodthings.SweetVh
import com.blood.bloodthings.currentSweetUnit
import com.blood.bloodthings.databinding.CardSweetBinding
import com.blood.bloodthings.mgUnit
import com.blood.bloodthings.mmolUnit
import com.blood.bloodthings.sweetState1
import com.blood.bloodthings.sweetState2
import com.blood.bloodthings.sweetState3
import com.blood.bloodthings.sweetState4
import com.blood.bloodthings.toDate3

class ThirdAdapter : RecyclerView.Adapter<SweetVh>() {

    var data = arrayListOf<SweetData>()
    var cardClick: ((SweetData) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SweetVh {
        val view = CardSweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SweetVh(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SweetVh, position: Int) {

        with(holder.bView) {
            val data1 = String.format("%.1f", data[position].dataMg)
            val data2 = String.format("%.1f", data[position].dataMMol)


            titleTxt.text = data[position].title
            sweetTxt.text =
                if (currentSweetUnit == mgUnit) data1 + mgUnit else data2 + mmolUnit
            dateTxt.text = data[position].time?.toDate3()
            copyImg.visibility =
                if (data[position].note.isNullOrEmpty()) View.GONE else View.VISIBLE
            markState.text = data[position].mark


            when (data[position].title) {
                sweetState1 -> {
                    stateLogo.setImageResource(R.mipmap.sweet_blue)
                    cardMsg.setBackgroundResource(R.drawable.item_bg_1)
                    markState.setBackgroundResource(R.drawable.sweet_btn_bg1)
                }

                sweetState2 -> {
                    stateLogo.setImageResource(R.mipmap.sweet_green)
                    cardMsg.setBackgroundResource(R.drawable.item_bg_2)
                    markState.setBackgroundResource(R.drawable.sweet_btn_bg2)
                }

                sweetState3 -> {
                    stateLogo.setImageResource(R.mipmap.sweet_yellow)
                    cardMsg.setBackgroundResource(R.drawable.item_bg_3)
                    markState.setBackgroundResource(R.drawable.sweet_btn_bg3)
                }

                sweetState4 -> {
                    stateLogo.setImageResource(R.mipmap.sweet_red)
                    cardMsg.setBackgroundResource(R.drawable.item_bg_4)
                    markState.setBackgroundResource(R.drawable.sweet_btn_bg4)
                }
            }

            cardSweet.setOnClickListener {
                cardClick?.invoke(data[position])
            }
        }
    }
}