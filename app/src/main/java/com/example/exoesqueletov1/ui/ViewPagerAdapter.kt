package com.example.exoesqueletov1.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentActivity?, private val list:List<Fragment>) : FragmentStateAdapter(fm!!) {

    override fun getItemCount() = list.size

    override fun createFragment(position: Int) = list[position]
}