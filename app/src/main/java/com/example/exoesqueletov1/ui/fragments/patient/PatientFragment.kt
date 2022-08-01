package com.example.exoesqueletov1.ui.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.FragmentPatientBinding
import com.example.exoesqueletov1.ui.ViewPagerAdapter
import com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.ConsultationsFragment
import com.example.exoesqueletov1.ui.fragments.patient.ui.expedients.ExpedientsFragment
import com.example.exoesqueletov1.ui.fragments.patient.ui.rutina.RutinasFragment
import com.example.exoesqueletov1.utils.Utils.startEndAnimation
import com.example.exoesqueletov1.utils.Utils.startFirstAnimation
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private val TAB_TITLES = arrayOf(
    "Consultas",
    "Rutinas",
    "Expediente",
)

@AndroidEntryPoint
class PatientFragment : Fragment() {

    private lateinit var viewModel: PatientViewModel
    private lateinit var binding: FragmentPatientBinding
    private var contactData = true

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
        val listFragment = listOf(
            ConsultationsFragment {
                viewModel.setConsultation(it.id)
                findNavController().navigate(R.id.action_patientFragment2_to_consultationFragment)
            },
            RutinasFragment() {
                viewModel.goRutina(it)
                findNavController().navigate(R.id.action_patientFragment2_to_navigation_paired_device)
            },
            ExpedientsFragment(),
        )
        val adapter = ViewPagerAdapter(activity, listFragment)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
        viewModel.patient.observe(viewLifecycleOwner) {
            binding.patient = it
        }
        binding.buttonAddConsult.setOnClickListener {
            findNavController().navigate(R.id.action_patientFragment2_to_medicalConsultationFragment)
        }
        binding.buttonWalk.setOnClickListener {
            findNavController().navigate(R.id.action_patientFragment2_to_asignaRutinasFragment)
        }
        binding.buttonClose.setOnClickListener {
            contactData = if (binding.cardContactData.visibility == View.GONE) {
                binding.cardContactData.startFirstAnimation(requireActivity())
                false
            } else {
                binding.cardContactData.startEndAnimation(requireActivity())
                true
            }
        }
    }

}