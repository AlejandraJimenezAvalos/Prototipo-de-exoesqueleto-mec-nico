package com.example.exoesqueletov1.ui.fragments.medical_consultation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.data.local.sharedpreferences.GradosObservaciones
import com.example.exoesqueletov1.databinding.FragmentMedicalConsultationBinding
import com.example.exoesqueletov1.databinding.SectionGradosObservacionesBinding
import com.example.exoesqueletov1.ui.dialogs.DialogEvaluacion
import com.example.exoesqueletov1.utils.Utils.getText
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MedicalConsultationFragment : Fragment() {

    private lateinit var binding: FragmentMedicalConsultationBinding
    private lateinit var viewModel: MedicalConsultationViewModel
    private var consultationTemporary = ConsultationTemporary()
    private val list = mutableListOf<SectionGradosObservacionesBinding>()

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

        binding.chipDolor.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipDolor.getChildAt(it) as Chip
                if (chip.isChecked) consultationTemporary.dolor = chip.text.toString()
            }
        }
        binding.chipMusculoSI.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipMusculoSI.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.musculoSuperiorIzquierdo = chip.text.toString()
            }
        }
        binding.chipMusculoSD.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipMusculoSD.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.musculoSuperiorDerecho = chip.text.toString()
            }
        }
        binding.chipMusculoII.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipMusculoII.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.musculoInferiorIzquierdo = chip.text.toString()
            }
        }
        binding.chipMusculoID.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipMusculoID.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.musculoInferiorDerecho = chip.text.toString()
            }
        }
        binding.chipTroncoD.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipTroncoD.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.troncoDerecho = chip.text.toString()
            }
        }
        binding.chipTroncoI.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipTroncoI.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.troncoIzquierdo = chip.text.toString()
            }
        }
        binding.chipCuelloI.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipCuelloI.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.cuelloIzquierdo = chip.text.toString()
            }
        }
        binding.chipCuelloD.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val chip = binding.chipCuelloD.getChildAt(it) as Chip
                if (chip.isChecked)
                    consultationTemporary.cuelloDerecho = chip.text.toString()
            }
        }

        binding.groupPiesJuntos.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.getChildAt(checkedId)!! as RadioButton
            consultationTemporary.pruebasEquilibrio = when (radio.text) {
                "No se sostuvo durante 10 segundos" -> 1
                "Se sostuvo durante 10 segundos" -> 2
                "No lo intento" -> 3
                else -> 1
            }
        }
        binding.groupA.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.getChildAt(checkedId)!! as RadioButton
            consultationTemporary.pruebasEquilibrioA = when (radio.text) {
                "No se sostuvo durante 10 segundos" -> 1
                "Se sostuvo durante 10 segundos" -> 2
                "No lo intento" -> 3
                else -> 1
            }
        }
        binding.groupB.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.getChildAt(checkedId)!! as RadioButton
            consultationTemporary.pruebasEquilibrioB = when (radio.text) {
                "No se sostuvo durante 10 segundos" -> 1
                "Se sostuvo durante 10 segundos" -> 2
                "No lo intento" -> 3
                else -> 1
            }
        }
        binding.groupC.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.getChildAt(checkedId)!! as RadioButton
            consultationTemporary.pruebasEquilibrio = when (radio.text) {
                "Se sostuvo durante 10 segundos" -> 1
                "No se sostuvo durante 3 a 9 segundos" -> 2
                "No se sostuvo durante 3 segundos" -> 3
                "No lo intento" -> 4
                else -> 1
            }
        }
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

    }

    override fun onPause() {
        super.onPause()
        viewModel.saveConsultation(saveConsultation())
    }

    private fun saveConsultation(): ConsultationTemporary {
        val listConsultation = mutableListOf<GradosObservaciones>()
        consultationTemporary.listOfGrados.clear()
        listConsultation.clear()
        for (i in list.indices) {
            val sectionBinding = list[i]
            val gradosObservaciones = GradosObservaciones()
            gradosObservaciones.grados = sectionBinding.layoutGrados.getText()
            gradosObservaciones.observaciones = sectionBinding.layoutObservaciones.getText()
            gradosObservaciones.position = i

            if (gradosObservaciones.isNotEmpty()) listConsultation.add(gradosObservaciones)
        }
        binding.apply {
            consultationTemporary.motivo = layoutMotivo.getText()
            consultationTemporary.sintomatologia = layoutSintomatologia.getText()
            consultationTemporary.pesoKg = layoutPesoK.getText()
            consultationTemporary.pesoG = layoutPesoG.getText()
            consultationTemporary.talla = layoutTalla.getText()
            consultationTemporary.estaturaM = layoutEstaturaM.getText()
            consultationTemporary.estaturaCm = layoutEstaturaCm.getText()
            consultationTemporary.libre = checkLibre.isChecked
            consultationTemporary.claudicante = checkClaudicante.isChecked
            consultationTemporary.conAyuda = checkConAyuda.isChecked
            consultationTemporary.espasticas = checkEspasticas.isChecked
            consultationTemporary.ataxica = checkAtaxica.isChecked
            consultationTemporary.observaciones = layoutObservaciones.getText()
            consultationTemporary.reflejos = layoutReflejos.getText()
            consultationTemporary.sensibilidad = layoutSensibilidad.getText()
            consultationTemporary.lenguaje = layoutLenguaje.getText()
            consultationTemporary.otros = layoutOtros.getText()
            //consultation.dolor = chipDolor
            //consultation.musculoSuperiorIzquierdo = chipMusculoSI
            //consultation.musculoSuperiorDerecho = chipMusculoSD
            //consultation.musculoInferiorIzquierdo = chipMusculoII
            //consultation.musculoInferiorDerecho = chipMusculoID
            //consultation.troncoIzquierdo = textTroncoI
            //consultation.troncoDerecho = textTroncoD
            //consultation.cuelloIzquierdo = chipCuelloI
            //consultation.cuelloDerecho = chipCuelloD
            consultationTemporary.valoracionInicial = layoutValoracion.getText()
            consultationTemporary.subjetivo = layoutSubjetivo.getText()
            consultationTemporary.analisis = layoutAnalisis.getText()
            consultationTemporary.planAccion = layoutPlan.getText()
            consultationTemporary.inicioMarcha = checkStart.isChecked
            consultationTemporary.pieDerechoNoSobrepasa = checkPieDerechoSobrepasa.isChecked
            consultationTemporary.pieDerechoNoLevanta = checkPieDerechoSobrepasa.isChecked
            consultationTemporary.pieIzquierdoNoSobrepasa = checkPieIzquierdoSobrepasa.isChecked
            consultationTemporary.pieIzquierdoNoLevanta = checkPieIzquierdoLevantamiento.isChecked
            //consultation.simetria =
            consultationTemporary.longitud = checkLogitudPaso.isChecked
            consultationTemporary.continuidad = checkContinuidadPaso.isChecked
            consultationTemporary.trayectoriaDesviacionAlta =
                checkTayectoriaDesviacionMarcada.isChecked
            consultationTemporary.trayectoriaDesviacionMedia =
                checkTrayectoriaDesviacionModerada.isChecked
            consultationTemporary.trayectoriaDesviacionNula = checkTayectoriaDesviacion.isChecked
            consultationTemporary.noBalanceoAlto = checkTroncoBalanceoAyuda.isChecked
            consultationTemporary.noBalanceoMedio = checkTroncoFlexion.isChecked
            consultationTemporary.noBalanceoNulo = checkTroncoNada.isChecked
            consultationTemporary.talones = checkTalonesSeparados.isChecked
            consultationTemporary.listOfGrados.addAll(listConsultation)
            //consultation.pruebasEquilibrio = groupPiesJuntos
            //consultation.pruebasEquilibrioA = groupA
            //consultation.pruebasEquilibrioB = groupB
            //consultation.pruebasEquilibrioC = groupC
            consultationTemporary.segundosMenor10 = switchSegundos.isChecked
            consultationTemporary.segundos = layoutSegundos.getText()
            consultationTemporary.objetivos = layoutObjetivos.getText()
            consultationTemporary.hipotesis = layoutHipotesis.getText()
            consultationTemporary.estructuraCorporal = layoutEstructuraCorporal.getText()
            consultationTemporary.funcionCorporal = layoutFunsionCorporal.getText()
            consultationTemporary.actividad = layoutActividad.getText()
            consultationTemporary.participacion = layoutParticipacion.getText()
            consultationTemporary.diagnostico = layoutDiagnostico.getText()
            consultationTemporary.plan = layoutPlanFinal.getText()
            return consultationTemporary
        }
    }

}