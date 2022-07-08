package com.example.exoesqueletov1.ui.fragments.medical_consultation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.databinding.FragmentMedicalConsultationBinding
import com.example.exoesqueletov1.ui.dialogs.DialogEvaluacion
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MedicalConsultationFragment : Fragment() {

    private lateinit var binding: FragmentMedicalConsultationBinding
    private lateinit var viewModel: MedicalConsultationViewModel
    private var consultationTemporary = ConsultationTemporary()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicalConsultationBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[MedicalConsultationViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.patient.observe(viewLifecycleOwner) {
            binding.patient = it
        }
        binding.imageInfo1.setOnClickListener {
            val dialog = DialogEvaluacion()
            dialog.show(childFragmentManager, "")
        }
    }

    override fun onStart() {
        super.onStart()
        consultationTemporary = viewModel.getConsultation()
        if (consultationTemporary.id.isEmpty()) consultationTemporary.id =
            UUID.randomUUID().toString()
        binding.consultation = consultationTemporary
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveConsultation(consultationTemporary)
    }

}