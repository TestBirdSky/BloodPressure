package com.blood.bloodthings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blood.bloodthings.PressureData
import com.blood.bloodthings.PressureVh
import com.blood.bloodthings.R
import com.blood.bloodthings.bluePressureStateStr
import com.blood.bloodthings.databinding.CardBloodBinding
import com.blood.bloodthings.greenPressureStateStr
import com.blood.bloodthings.lighterRedPressureStateStr
import com.blood.bloodthings.orangePressureStateStr
import com.blood.bloodthings.redPressureStateStr
import com.blood.bloodthings.toDate3
import com.blood.bloodthings.yellowPressureStateStr

class SecondAdapter : RecyclerView.Adapter<PressureVh>() {

    var data = arrayListOf<PressureData>()
    var cardClick: ((PressureData) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureVh {
        val view = CardBloodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PressureVh(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PressureVh, position: Int) {
        with(holder.bView) {
            when (data[position].title) {
                bluePressureStateStr -> {
                    stateLogo.setImageResource(R.mipmap.pressure1)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_1)
                }

                greenPressureStateStr -> {
                    stateLogo.setImageResource(R.mipmap.pressure2)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_2)
                }

                yellowPressureStateStr -> {
                    stateLogo.setImageResource(R.mipmap.pressure3)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_3)
                }

                orangePressureStateStr -> {
                    stateLogo.setImageResource(R.mipmap.pressure4)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_4)
                }

                lighterRedPressureStateStr -> {
                    stateLogo.setImageResource(R.mipmap.pressure6)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_5)
                }

                redPressureStateStr -> {
                    stateLogo.setImageResource(R.mipmap.pressure5)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_6)
                }

                else -> {
                    stateLogo.setImageResource(R.mipmap.pressure2)
                    cardDetail.setBackgroundResource(R.drawable.item_bg_2)
                }
            }

            titleTxt.text = data[position].title
            dateTxt.text = data[position].time?.toDate3()
            valueTxt1.text = data[position].sys.toString()
            valueTxt2.text = data[position].dia.toString()
            valueTxt3.text = data[position].pul.toString()
            copyImg.visibility =
                if (data[position].note.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            pressureCard.setOnClickListener {
                cardClick?.invoke(data[position])
            }
        }
    }
}