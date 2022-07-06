package com.example.exoesqueletov1.ui.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.databinding.FragmentPatientBinding
import com.example.exoesqueletov1.ui.fragments.patient.adapter.ExpedientAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientFragment : Fragment() {

    private lateinit var viewModel: PatientViewModel
    private lateinit var binding: FragmentPatientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        binding = FragmentPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf<ExpedientModel>()
        val adapter = ExpedientAdapter(list)
        binding.recyclerExpedient.setHasFixedSize(true)
        binding.recyclerExpedient.adapter = adapter
        viewModel.patient.observe(viewLifecycleOwner) {
            binding.patient = it
        }
        viewModel.expedient.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

}