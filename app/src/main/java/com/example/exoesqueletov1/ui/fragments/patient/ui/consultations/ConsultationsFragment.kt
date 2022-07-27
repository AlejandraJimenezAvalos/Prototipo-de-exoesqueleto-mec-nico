package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.models.consultation.ConsultationData
import com.example.exoesqueletov1.databinding.FragmentConsultationsBinding
import com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.adapter.ConsultationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsultationsFragment(private val function: (ConsultationData) -> Unit) : Fragment() {

    private lateinit var binding: FragmentConsultationsBinding
    private lateinit var viewModel: ConsultationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsultationsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[ConsultationsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listConsultation = mutableListOf<ConsultationData>()
        val adapter = ConsultationAdapter(listConsultation, function)
        binding.recyclerConsultation.setHasFixedSize(true)
        binding.recyclerConsultation.adapter = adapter
        viewModel.consultations.observe(viewLifecycleOwner) {
            listConsultation.clear()
            listConsultation.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

}