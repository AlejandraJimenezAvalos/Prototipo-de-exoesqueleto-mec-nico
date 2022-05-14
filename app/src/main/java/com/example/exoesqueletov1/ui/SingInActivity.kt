package com.example.exoesqueletov1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.clases.ViewPagerAdapter
import com.example.exoesqueletov1.ui.fragments.WelcomeFragment
import com.example.exoesqueletov1.ui.fragments.login.LogInFragment
import com.example.exoesqueletov1.ui.fragments.SingUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        val viewPager = findViewById<ViewPager>(R.id.pager)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(WelcomeFragment())
        adapter.addFragment(LogInFragment())
        adapter.addFragment(SingUpFragment())
        viewPager.adapter = adapter
    }
}