package com.example.exoesqueletov1.ui.fragments.medical_consultation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary.Companion.getConsultationTemporary
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary.Companion.isValid
import com.example.exoesqueletov1.data.models.Consultation.Companion.getConsultation
import com.example.exoesqueletov1.databinding.FragmentMedicalConsultationBinding
import com.example.exoesqueletov1.databinding.SectionGradosObservacionesBinding
import com.example.exoesqueletov1.utils.Utils.startEndAnimation
import com.example.exoesqueletov1.utils.Utils.startFirstAnimation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MedicalConsultationFragment : Fragment() {

    private lateinit var binding: FragmentMedicalConsultationBinding
    private lateinit var viewModel: MedicalConsultationViewModel
    private val list = mutableListOf<SectionGradosObservacionesBinding>()
    private lateinit var consultationTemporary: ConsultationTemporary

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
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.ic_round_assignment_24)
                .setTitle("Evalucación")
                .setMessage(
                    "La fuerza del paciente esta graduada en una escala del 0 al 5\n\n\n" +
                            "5 Fuerza muscular normal contra la resistencia completa.\n\n" +
                            "4 La fuerza muscular esta reducida, pero la contracción muscular " +
                            "puede realizar un movimiento articular contra la resistencia.\n\n" +
                            "3 La fuerza muscular está reducida tanto que el movimiento " +
                            "articular solo puede realizarse contra la gravedad, sin la " +
                            "resistencia del examinador. Por ejemplo, la articulación del " +
                            "codo puede moverse desde extensión completa hasta flexión " +
                            "completa, comenzando con el brazo suspendido al lado del cuerpo.\n\n" +
                            "2 Movimiento activo que no puede vencer la fuerza de gavedad. Por " +
                            "ejemplo, el codo puede flexionarse completamente solo cuando el " +
                            "brazo es mantenido en un plano horizontal.\n\n" +
                            "1 Esbozo de contracción muscular.\n\n" +
                            "0 Ausencia de contracción muscular."
                )
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        binding.fabAddData.setOnClickListener {
            if (binding.isValid()) {
                viewModel.saveConsultation(
                    binding.getConsultationTemporary(
                        list,
                        consultationTemporary
                    ).getConsultation()
                )
            }
        }
    }

    // region Write Temporary Data
    override fun onStart() {
        super.onStart()
        consultationTemporary = viewModel.getConsultation()
        if (consultationTemporary.id.isEmpty()) consultationTemporary.id =
            UUID.randomUUID().toString()
        binding.consultation = consultationTemporary
        // region Set Listeners of ChipGroup
        binding.chipDolor.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.dolor = getSelectedText(group, checkedId)
        }
        binding.chipMusculoSI.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.musculoSuperiorIzquierdo = getSelectedText(group, checkedId)
        }
        binding.chipMusculoSD.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.musculoSuperiorDerecho = getSelectedText(group, checkedId)
        }
        binding.chipMusculoII.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.musculoInferiorIzquierdo = getSelectedText(group, checkedId)
        }
        binding.chipMusculoID.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.musculoInferiorDerecho = getSelectedText(group, checkedId)
        }
        binding.chipTroncoD.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.troncoDerecho = getSelectedText(group, checkedId)
        }
        binding.chipTroncoI.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.troncoIzquierdo = getSelectedText(group, checkedId)
        }
        binding.chipCuelloI.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.cuelloIzquierdo = getSelectedText(group, checkedId)
        }
        binding.chipCuelloD.setOnCheckedChangeListener { group, checkedId ->
            consultationTemporary.cuelloDerecho = getSelectedText(group, checkedId)
        }
        // endregion
        // region Set Listeners of radiogroups
        binding.groupA.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)!!
            consultationTemporary.pruebasEquilibrioA = when (radio.text) {
                "No se sostuvo durante 10 segundos" -> 1
                "Se sostuvo durante 10 segundos" -> 2
                "No lo intento" -> 3
                else -> 1
            }
        }
        binding.groupB.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)!!
            consultationTemporary.pruebasEquilibrioB = when (radio.text) {
                "No se sostuvo durante 10 segundos" -> 1
                "Se sostuvo durante 10 segundos" -> 2
                "No lo intento" -> 3
                else -> 1
            }
        }
        binding.groupC.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)!!
            consultationTemporary.pruebasEquilibrioC = when (radio.text) {
                "Se sostuvo durante 10 segundos" -> 1
                "No se sostuvo durante 3 a 9 segundos" -> 2
                "No se sostuvo durante 3 segundos" -> 3
                "No lo intento" -> 4
                else -> 1
            }
        }
        // endregion
        // region List of section grados/observaciones
        binding.apply {
            list.add(lateralCabeza)
            list.add(cabezaRotada)
            list.add(asimetriaMaxilar)
            list.add(claviculasAsimetricas)
            list.add(hombroCaido)
            list.add(hombroElevado)
            list.add(cubitoValgo)
            list.add(cubitoVaro)
            list.add(rotacionInternaCadera)
            list.add(rotacionExternaCadera)
            list.add(genuVarum)
            list.add(genuValgum)
            list.add(torsionTibialInterna)
            list.add(torsionTibialExterna)
            list.add(halluxValgus)
            list.add(dedosGarra)
            list.add(dedosMartillo)
            list.add(desplazamientoAnteriorCuerpo)
            list.add(desplazamientoPosteriorCuerpo)
            list.add(cabezaAdelantada)
            list.add(cifosis)
            list.add(pectusExcavatum)
            list.add(pechoTonel)
            list.add(pectusCarinatum)
            list.add(columna)
            list.add(espaldaCifotica)
            list.add(espaldaPlana)
            list.add(inclinacionAnterior)
            list.add(inclinacionPosterior)
            list.add(genuRecurvatum)
            list.add(rodillasFlexionadas)
            list.add(cabezaInclinada)
            list.add(cabezaRotadaPosterior)
            list.add(hombroCaidoPosterior)
            list.add(hombroElevadoPosterior)
            list.add(espaldaPlanaPsterior)
            list.add(absuccionEscapulas)
            list.add(adiccionEscapulas)
            list.add(escapulasAladas)
            list.add(curvaturaLateral)
            list.add(rotacionInternaCaderaTronco)
            list.add(rotacionExternaCaderaPosterior)
            list.add(inclinacionLateralPelvis)
            list.add(rotacionPelvica)
            list.add(caderaAbducida)
            list.add(piePronado)
            list.add(pieSupinado)
            list.add(piePlano)
            list.add(pieCavo)
        }
        // endregion
        // region Set list of Evaluacion Muscular
        if (consultationTemporary.listOfGrados.isNotEmpty() && list.isNotEmpty())
            for (i in consultationTemporary.listOfGrados.indices) {
                val position = consultationTemporary.listOfGrados[i].position
                if (position <= list.lastIndex) list[position].datos =
                    consultationTemporary.listOfGrados[i]
                Log.e(
                    "Result list: list $i",
                    "${consultationTemporary.listOfGrados[i].grados} ${consultationTemporary.listOfGrados[i].observaciones}"
                )
            }
        // endregion
        // region Set listeners of checks and switch
        binding.apply {
            switchSegundos.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) layoutSegundos.startFirstAnimation(requireActivity())
                else layoutSegundos.startEndAnimation(requireActivity())
            }
            checkConsult.setListener(cardConsult)
            checkFisicalExploration.setListener(cardFisicalExploration)
            checkMarcha.setListener(cardMarcha)
            checkDiagnostico.setListener(cardDiagnostico)
            checkPain.setListener(cardPain)
            checkEvalucionMuscular.setListener(cardEvalucionMuscular)
            checkAnalisisMarcha.setListener(cardAnalisisMarcha)
            checkEvaluacionPostura.setListener(cardEvaluacionPostura)
            checkForm2.setListener(cardForm2)
            checkPlan.setListener(cardPlan)
        }
        // endregion
    }
    // endregion

    override fun onPause() {
        super.onPause()
        viewModel.saveConsultation(binding.getConsultationTemporary(list, consultationTemporary))
    }

    private fun CheckBox.setListener(view: View) {
        this.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) view.startFirstAnimation(requireActivity())
            else view.startEndAnimation(requireActivity())
        }
    }

    private fun getSelectedText(chipGroup: ChipGroup, id: Int): String {
        val mySelection = chipGroup.findViewById<Chip>(id)
        return mySelection?.text?.toString() ?: ""
    }

}