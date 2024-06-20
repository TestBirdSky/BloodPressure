package com.blood.bloodthings.smallPage

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.blood.bloodthings.PApp
import com.blood.bloodthings.SweetData
import com.blood.bloodthings.adapter.ThirdAdapter
import com.blood.bloodthings.bigPage.FifthActivity
import com.blood.bloodthings.bigPage.FourthActivity
import com.blood.bloodthings.currentSweetEntity
import com.blood.bloodthings.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    private lateinit var viewBinding: FragmentSecondBinding

    private val thirdAdapter: ThirdAdapter by lazy {
        ThirdAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSecondBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        viewBinding.urlTxt.paintFlags = viewBinding.urlTxt.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        viewBinding.sweetIc.setOnClickListener {
            startActivity(Intent(requireActivity(), FourthActivity::class.java))
        }
        viewBinding.cardPrivacy.setOnClickListener {
            startActivity(Intent(requireActivity(), FifthActivity::class.java))
        }
    }


    private fun initRv() {
        thirdAdapter.cardClick = {
            currentSweetEntity = it
            startActivity(Intent(requireActivity(), FourthActivity::class.java))
        }
        with(viewBinding) {
            sweetRv.apply {
                adapter = thirdAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }
        val dataSet = PApp.SweetDao.all
        if (dataSet.isEmpty()) {
            viewBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewBinding.emptyView.visibility = View.GONE
        }
        if (dataSet.isEmpty() || dataSet.size < 2) {
            thirdAdapter.data = dataSet as ArrayList<SweetData>
            thirdAdapter.notifyDataSetChanged()
        } else {
            thirdAdapter.data = dataSet.reversed() as ArrayList<SweetData>
            thirdAdapter.notifyDataSetChanged()
        }
    }

    fun reloadData() {
        val dataSet = PApp.SweetDao.all
        try {
            if (dataSet.isEmpty()) {
                viewBinding.emptyView.visibility = View.VISIBLE
            } else {
                viewBinding.emptyView.visibility = View.GONE
            }
        } catch (e: Exception) {

        }
        if (dataSet.isEmpty() || dataSet.size < 2) {
            thirdAdapter.data = dataSet as ArrayList<SweetData>
            thirdAdapter.notifyDataSetChanged()
        } else {
            thirdAdapter.data = dataSet.reversed() as ArrayList<SweetData>
            thirdAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondFragment()
    }
}