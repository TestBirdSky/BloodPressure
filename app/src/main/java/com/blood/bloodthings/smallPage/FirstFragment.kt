package com.blood.bloodthings.smallPage

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.blood.bloodthings.PApp
import com.blood.bloodthings.PressureData
import com.blood.bloodthings.adapter.SecondAdapter
import com.blood.bloodthings.bigPage.FifthActivity
import com.blood.bloodthings.bigPage.ThirdActivity
import com.blood.bloodthings.currentPressureEntity
import com.blood.bloodthings.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private lateinit var viewBinding: FragmentFirstBinding

    private val pressureAdapter: SecondAdapter by lazy {
        SecondAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentFirstBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pressureAdapter.cardClick = {
            currentPressureEntity = it
            startActivity(Intent(requireActivity(), ThirdActivity::class.java))
        }
        with(viewBinding) {
            urlTxt.paintFlags = urlTxt.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            cardPrivacy.setOnClickListener {
                startActivity(Intent(requireActivity(), FifthActivity::class.java))
            }
            pressureIc.setOnClickListener {
                startActivity(Intent(requireActivity(), ThirdActivity::class.java))
            }
            with(pressureRv) {
                adapter = pressureAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }
        val dataSet = PApp.PressDao.all
        if (dataSet.isEmpty()) {
            viewBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewBinding.emptyView.visibility = View.GONE
        }
        if (dataSet.isEmpty() || dataSet.size < 2) {
            pressureAdapter.data = dataSet as ArrayList<PressureData>
            pressureAdapter.notifyDataSetChanged()
        } else {
            pressureAdapter.data = dataSet.reversed() as ArrayList<PressureData>
            pressureAdapter.notifyDataSetChanged()
        }
    }

    fun reloadData() {
        val dataSet = PApp.PressDao.all
        try {
            if (dataSet.isEmpty()) {
                viewBinding.emptyView.visibility = View.VISIBLE
            } else {
                viewBinding.emptyView.visibility = View.GONE
            }
        }catch (e:Exception){

        }

        if (dataSet.isEmpty() || dataSet.size < 2) {
            pressureAdapter.data = dataSet as ArrayList<PressureData>
            pressureAdapter.notifyDataSetChanged()
        } else {
            pressureAdapter.data = dataSet.reversed() as ArrayList<PressureData>
            pressureAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FirstFragment()
    }
}