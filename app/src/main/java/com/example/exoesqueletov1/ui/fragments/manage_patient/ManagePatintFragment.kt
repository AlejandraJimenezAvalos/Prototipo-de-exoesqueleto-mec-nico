package com.example.exoesqueletov1.ui.fragments.manage_patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R

class ManagePatintFragment : Fragment() {

    companion object {
        fun newInstance() = ManagePatintFragment()
    }

    private lateinit var viewModel: ManagePatintViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_patint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ManagePatintViewModel::class.java]
    }

}