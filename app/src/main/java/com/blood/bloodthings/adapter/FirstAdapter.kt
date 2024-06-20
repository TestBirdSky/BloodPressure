package com.blood.bloodthings.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blood.bloodthings.smallPage.FirstFragment
import com.blood.bloodthings.smallPage.SecondFragment

class FirstAdapter(page: FragmentActivity) : FragmentStateAdapter(page) {

    private val pages = arrayListOf(FirstFragment.newInstance(), SecondFragment.newInstance())

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }

    fun reloadData() {
        (pages[0] as FirstFragment).reloadData()
        (pages[1] as SecondFragment).reloadData()
    }
}