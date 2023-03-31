package com.example.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.detail.FollowerFragment
import com.example.githubuser.ui.detail.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val login: String?): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowingFragment()
                fragment.arguments = Bundle().apply {
                    putInt(FollowingFragment.ARG_SECTION_NUMBER, position)
                    putString(FollowingFragment.ARG_SECTION_USERNAME, login.toString())
                }
            }
            1 -> {
                fragment = FollowerFragment()
                fragment.arguments = Bundle().apply {
                    putInt(FollowerFragment.ARG_SECTION_NUMBER, position)
                    putString(FollowerFragment.ARG_SECTION_USERNAME, login.toString())
                }
            }
        }
        return fragment as Fragment
    }
}