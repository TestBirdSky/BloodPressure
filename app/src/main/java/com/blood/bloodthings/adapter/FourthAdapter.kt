package com.blood.bloodthings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blood.bloodthings.PressureTypeVh
import com.blood.bloodthings.R
import com.blood.bloodthings.SweetData
import com.blood.bloodthings.SweetVh
import com.blood.bloodthings.databinding.CardSweetBinding
import com.blood.bloodthings.databinding.ItemPressureTypeBinding

class FourthAdapter : RecyclerView.Adapter<PressureTypeVh>() {

    var data = arrayListOf("1", "2", "3", "4", "5", "6")
    var currentType = "2"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureTypeVh {
        val view =
            ItemPressureTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PressureTypeVh(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PressureTypeVh, position: Int) {
        with(holder.bView) {
            when (data[position]) {
                "1" -> viewType.setBackgroundResource(R.drawable.item_type_1)
                "2" -> viewType.setBackgroundResource(R.drawable.item_type_2)
                "3" -> viewType.setBackgroundResource(R.drawable.item_type_3)
                "4" -> viewType.setBackgroundResource(R.drawable.item_type_4)
                "5" -> viewType.setBackgroundResource(R.drawable.item_type_5)
                "6" -> viewType.setBackgroundResource(R.drawable.item_type_6)
            }
            typeIndicator.visibility = if (data[position] == currentType) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }
}