package com.example.exoesqueletov1.ui.fragments.patient.ui.walk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.FragmentWalkBinding

class WalkFragment : Fragment() {

    private lateinit var binding: FragmentWalkBinding
    private lateinit var viewModel: WalkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[WalkViewModel::class.java]
        return inflater.inflate(R.layout.fragment_walk, container, false)
    }

}