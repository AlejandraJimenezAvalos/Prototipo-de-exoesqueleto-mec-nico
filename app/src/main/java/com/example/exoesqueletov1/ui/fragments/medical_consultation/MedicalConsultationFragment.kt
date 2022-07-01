package com.example.exoesqueletov1.ui.fragments.medical_consultation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R

class MedicalConsultationFragment : Fragment() {

    companion object {
        fun newInstance() = MedicalConsultationFragment()
    }

    private lateinit var viewModel: MedicalConsultationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical_consultation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MedicalConsultationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}