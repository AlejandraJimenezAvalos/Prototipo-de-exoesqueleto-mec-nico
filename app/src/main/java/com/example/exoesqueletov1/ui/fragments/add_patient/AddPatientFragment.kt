package com.example.exoesqueletov1.ui.fragments.add_patient

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.local.sharedpreferences.PatientTemporary
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.databinding.FragmentAddPatientBinding
import com.example.exoesqueletov1.ui.fragments.add_patient.adapter.ExpedientAdapter
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getGender
import com.example.exoesqueletov1.utils.Utils.getText
import com.example.exoesqueletov1.utils.Utils.isNotEmpty
import com.example.exoesqueletov1.utils.Utils.isValidEmail
import com.example.exoesqueletov1.utils.Utils.isValidPhone
import com.example.exoesqueletov1.utils.Utils.setText
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddPatientFragment : Fragment() {

    private lateinit var binding: FragmentAddPatientBinding

    private lateinit var viewModel: AddPatientViewModel
    private lateinit var date: Date
    private var patientTemporary = PatientTemporary()
    private lateinit var endAnimation: Animation
    private lateinit var startAnimation: Animation
    private val list = mutableListOf<ExpedientModel>()
    private var progressControl = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPatientBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[AddPatientViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        patientTemporary.idPatient = UUID.randomUUID().toString()
        endAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_scale_down)
        startAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_scale_up)
        endAnimation.duration = 400
        startAnimation.duration = 1000
        date = SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01")!!

        val adapter = ExpedientAdapter(list)

        binding.recyclerView3.setHasFixedSize(true)
        binding.recyclerView3.adapter = adapter

        binding.layoutEdad.editText!!.setRawInputType(InputType.TYPE_NULL)
        binding.layoutEdad.editText!!.setOnKeyListener(null)
        binding.layoutEdad.editText!!.setOnClickListener {
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

        binding.buttonOkPersonalData.setOnClickListener {
            val stateName = binding.layoutNombre.isNotEmpty("Campo requerido")
            val stateBirthday = binding.layoutEdad.isNotEmpty("Campo requerido")
            if (stateName && stateBirthday) {
                patientTemporary.apply {
                    name = binding.layoutNombre.getText()
                    birthday = binding.layoutEdad.getText()
                    gender =
                        if (binding.radioWomen.isChecked) Constants.Gender.Femenino.toString()
                        else Constants.Gender.Masculino.toString()
                    occupation = binding.layoutOccupation.getText()
                }
                startAnimation(
                    binding.cardPersonalData,
                    binding.cardContactData,
                    "Datos de contacto",
                    "1/5",
                    20
                )
            }
        }
        binding.buttonOkContact.setOnClickListener {
            val layoutPhone = binding.layoutPhone
            val layoutLada = binding.layoutLada
            var status = true
            if (binding.layoutEmail.getText().isNotEmpty())
                if (binding.layoutEmail.isValidEmail())
                    patientTemporary.email = binding.layoutEmail.getText()
                else status = false
            else binding.layoutEmail.error = null

            if (binding.layoutAddress.getText().isNotEmpty())
                patientTemporary.address = binding.layoutAddress.getText()

            if (layoutPhone.getText().isNotEmpty() && layoutLada.getText().isNotEmpty())
                if (layoutPhone.isValidPhone(layoutLada)) {
                    patientTemporary.phone = layoutPhone.getText()
                    patientTemporary.lada = layoutLada.getText()
                } else status = false
            else {
                binding.layoutPhone.error = null
                binding.layoutLada.error = null
            }
            if (status) {
                startAnimation(
                    binding.cardContactData,
                    binding.cardOtherData,
                    "Antecedentes patológicos y heredofamiliares",
                    "2/5",
                    40
                )
            }
        }
        binding.buttonOmitir.setOnClickListener {
            startAnimation(
                binding.cardContactData,
                binding.cardOtherData,
                "Antecedentes patológicos y heredofamiliares",
                "2/5",
                40
            )
        }
        binding.buttonAdd.setOnClickListener {
            val name = binding.layoutNameAntecedentes
            val value = binding.layoutValorAntecedentes
            if (name.isNotEmpty("Este campo es requerido") && value.isNotEmpty("Este campo es requerido")) {
                list.add(
                    ExpedientModel(
                        id = UUID.randomUUID().toString(),
                        value = value.getText(),
                        name = name.getText(),
                        idPatient = patientTemporary.idPatient,
                        type = when (progressControl) {
                            2 -> Constants.TypeData.Antecedents.toString()
                            3 -> Constants.TypeData.Habits.toString()
                            4 -> Constants.TypeData.Scar.toString()
                            else -> Constants.TypeData.Antecedents.toString()
                        },
                        idUser = Firebase.auth.currentUser!!.uid,
                    )
                )
                name.setText("")
                value.setText("")
                if (binding.recyclerView3.visibility == View.GONE) binding.recyclerView3.visibility =
                    View.VISIBLE

                adapter.notifyDataSetChanged()
            }
        }
        binding.buttonOk.setOnClickListener {
            binding.apply {
                when (progressControl) {
                    2 -> {
                        startAnimation(
                            cardOtherData,
                            cardOtherData,
                            "Habitos de salud",
                            "3/5",
                            60
                        )
                        patientTemporary.listAntecedents.addAll(list)
                        list.clear()
                        binding.textSugerencias.text =
                            "Sugerencias: Tabaquismo, Alcoholismo, Drogas, Actividades física, automedicasión, etc."
                        binding.recyclerView3.visibility = View.GONE
                    }
                    3 -> {
                        startAnimation(
                            cardOtherData,
                            cardOtherData,
                            "Cicatrices quirúrgicas",
                            "4/5",
                            80
                        )
                        patientTemporary.listHabits.addAll(list)
                        list.clear()
                        binding.textSugerencias.text =
                            "Sugerencias: Sitio, Retractil, Queloidem Abierta, con adherencia, hipertrófica, etc."
                        binding.recyclerView3.visibility = View.GONE
                        binding.buttonOk.visibility = View.GONE
                    }
                }
            }
        }
        binding.buttonSave.setOnClickListener {
            binding.textProgress.text = "5/5"
            binding.progress.progress = 100
            progressControl = 5
            patientTemporary.listCicatriz.addAll(list)
            viewModel.saveUser(patientTemporary)
            val status = findNavController().popBackStack()
            if (!status) findNavController().navigate(R.id.action_global_navigation_message)
        }
    }

    override fun onPause() {
        super.onPause()
        if (progressControl <= 4) viewModel.setPatientTemporary(patientTemporary)
    }

    override fun onResume() {
        super.onResume()
        patientTemporary = viewModel.getPatientTemporary()
        binding.patient = patientTemporary
        patientTemporary.apply {
            if (idPatient.isEmpty()) idPatient = UUID.randomUUID().toString()
            if (gender.getGender() == Constants.Gender.Femenino) binding.radioWomen.isChecked = true
            else binding.radioMen.isChecked = true
        }
    }

    private fun startAnimation(
        old: MaterialCardView,
        new: MaterialCardView,
        title: String,
        progressIndicator: String,
        progress: Int
    ) {
        endAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                old.visibility = View.GONE
                new.visibility = View.VISIBLE
                binding.textProgress.text = progressIndicator
                binding.progress.progress = progress
                binding.textTitle.text = title
                new.startAnimation(startAnimation)
                endAnimation.setAnimationListener(null)
                if (progressControl == 4) {
                    binding.buttonSave.startAnimation(startAnimation)
                    binding.buttonSave.visibility = View.VISIBLE
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        old.startAnimation(endAnimation)
        progressControl++
    }
}