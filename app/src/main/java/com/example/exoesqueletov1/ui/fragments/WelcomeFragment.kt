package com.example.exoesqueletov1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.databinding.FragmentWelcomeBinding

class WelcomeFragment(private val navigation: (Constants.SingInPagerNavigation) -> Unit) :
    Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val animation = AnimationUtils.loadAnimation(context, R.anim.transition)
        binding.textHola.animation = animation
        binding.textVamos.animation = animation
        binding.buttonGoToLogin.setOnClickListener {
            navigation.invoke(Constants.SingInPagerNavigation.Login)
        }
        binding.buttonDoYouHaveAccount.setOnClickListener {
            navigation.invoke(Constants.SingInPagerNavigation.SingIn)
        }
    }
}