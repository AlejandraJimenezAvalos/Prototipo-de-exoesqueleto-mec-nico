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
import com.example.exoesqueletov1.utils.Utils.getGender
import com.example.exoesqueletov1.utils.Utils.getText
import com.example.exoesqueletov1.utils.Utils.isNotEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientFragment : Fragment() {

    private lateinit var binding: FragmentPatientBinding

    private lateinit var viewModel: PatientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[PatientViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            val nameState = binding.layoutNombre.isNotEmpty("No se puede quedar vacio")
            val stateEdad = binding.layoutEdad.isNotEmpty("No se puede quedar vacio")
            val statePersonales = binding.layoutPersonales.isNotEmpty("No se puede quedar vacio")
            val stateFamiliares = binding.layoutFamiliares.isNotEmpty("No se puede quedar vacio")
            if (nameState && stateEdad && statePersonales && stateFamiliares) {
                viewModel.saveUser(
                    binding.layoutNombre.getText(),
                    binding.layoutEdad.getText(),
                    binding.radioWomen.getGender(),
                    binding.layoutOccupation.getText(),
                    binding.layoutPersonales.getText(),
                    binding.layoutFamiliares.getText(),
                    binding.layoutEmail.getText(),
                    binding.layoutPhone.getText(),
                    binding.layoutAddress.getText(),
                )
                val status = findNavController().popBackStack()
                if (!status) findNavController().navigate(R.id.action_global_navigation_message)
            }
        }
    }

}