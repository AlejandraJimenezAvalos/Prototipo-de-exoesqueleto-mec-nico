package com.example.exoesqueletov1.ui.fragments.patient

import android.os.Bundle
import android.text.InputType
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
import com.example.exoesqueletov1.utils.Utils.isValidEmail
import com.example.exoesqueletov1.utils.Utils.isValidPhone
import com.example.exoesqueletov1.utils.Utils.setText
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PatientFragment : Fragment() {

    private lateinit var binding: FragmentPatientBinding

    private lateinit var viewModel: PatientViewModel
    private lateinit var date: Date

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

        date = SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01")!!

        binding.layoutEdad.editText!!.setRawInputType(InputType.TYPE_NULL)
        binding.layoutEdad.editText!!.setOnKeyListener(null)
        binding.layoutEdad.editText!!.setOnClickListener { createDatePicker() }

        binding.buttonCancel.setOnClickListener {
            val status = findNavController().popBackStack()
            if (!status) findNavController().navigate(R.id.action_global_navigation_message)
        }

        binding.buttonSave.setOnClickListener {
            val nameState = binding.layoutNombre.isNotEmpty("No se puede quedar vacio")
            val stateEdad = binding.layoutEdad.isNotEmpty("No se puede quedar vacio")
            val statePersonales = binding.layoutPersonales.isNotEmpty("No se puede quedar vacio")
            val stateFamiliares = binding.layoutFamiliares.isNotEmpty("No se puede quedar vacio")
            val statePhoneValid = binding.layoutPhone.isValidPhone(binding.layoutLada)
            val stateEmail =
                if (binding.layoutEmail.editText!!.text.toString().isNotEmpty())
                    binding.layoutEmail.isValidEmail()
                else
                    true
            if (nameState && stateEdad && statePersonales && stateFamiliares && statePhoneValid && stateEmail) {
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

    private fun createDatePicker() {
        val datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText("Seleccione la fecha")
            .setSelection(if (date == null) MaterialDatePicker.todayInUtcMilliseconds() else date.time)
            .build()
        datePicker.show(childFragmentManager, "Select date")
        datePicker.addOnPositiveButtonClickListener {
            val timeZoneUTC: TimeZone = TimeZone.getDefault()
            val offsetFromUTC: Int = timeZoneUTC.getOffset(Date().time) * -1
            val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            date = Date(it + offsetFromUTC)
            binding.layoutEdad.setText(simpleFormat.format(date))
        }
    }

}