package com.example.exoesqueletov1.ui.activity.sing_in

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.ui.ViewPagerAdapter
import com.example.exoesqueletov1.ui.activity.main.MainActivity
import com.example.exoesqueletov1.ui.fragments.WelcomeFragment
import com.example.exoesqueletov1.ui.fragments.login.LogInFragment
import com.example.exoesqueletov1.ui.fragments.singin.SingInFragment
import com.example.exoesqueletov1.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingInActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val welcome = WelcomeFragment {
            if (it == Constants.SingInPagerNavigation.Login) viewPager.currentItem = 1
            else viewPager.currentItem = 2
        }
        val list = listOf(welcome, LogInFragment(), SingInFragment())
        val adapter = ViewPagerAdapter(this, list)
        viewPager.adapter = adapter
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener)
    }
}