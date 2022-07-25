package com.example.exoesqueletov1.data.local.sharedpreferences

import com.example.exoesqueletov1.databinding.FragmentMedicalConsultationBinding
import com.example.exoesqueletov1.databinding.SectionGradosObservacionesBinding
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getText
import com.example.exoesqueletov1.utils.Utils.isNotEmpty

class ConsultationTemporary {
    // region Declaration of variables
    var id = ""
    var idPatient = ""
    var idUser = ""
    var motivo = ""
    var sintomatologia = ""
    var pesoKg = ""
    var pesoG = ""
    var talla = ""
    var estaturaM = ""
    var estaturaCm = ""
    var libre = false
    var claudicante = false
    var conAyuda = false
    var espasticas = false
    var ataxica = false
    var observaciones = ""
    var reflejos = ""
    var sensibilidad = ""
    var lenguaje = ""
    var otros = ""
    var dolor = ""
    var musculoSuperiorIzquierdo = ""
    var musculoSuperiorDerecho = ""
    var musculoInferiorIzquierdo = ""
    var musculoInferiorDerecho = ""
    var troncoIzquierdo = ""
    var troncoDerecho = ""
    var cuelloIzquierdo = ""
    var cuelloDerecho = ""
    var valoracionInicial = ""
    var subjetivo = ""
    var analisis = ""
    var planAccion = ""
    var inicioMarcha = false
    var pieDerechoNoSobrepasa = false
    var pieDerechoNoLevanta = false
    var pieIzquierdoNoSobrepasa = false
    var pieIzquierdoNoLevanta = false
    var longitud = false
    var continuidad = false
    var trayectoriaDesviacionAlta = false
    var trayectoriaDesviacionMedia = false
    var trayectoriaDesviacionNula = false
    var noBalanceoAlto = false
    var noBalanceoMedio = false
    var noBalanceoNulo = false
    var talones = false
    var listOfGrados = mutableListOf<EvaluacionPosturaTemporary>()
    var pruebasEquilibrioA = 0
    var pruebasEquilibrioB = 0
    var pruebasEquilibrioC = 0
    var segundosMenor10 = false
    var segundos = ""
    var objetivos = ""
    var hipotesis = ""
    var estructuraCorporal = ""
    var funcionCorporal = ""
    var actividad = ""
    var participacion = ""
    var diagnostico = ""
    var plan = ""
    // endregion

    fun getDolorInt() = if (dolor.isEmpty()) 11 else dolor.toInt()
    fun getMusculoSuperiorIzquierdoInt() =
        if (musculoSuperiorIzquierdo.isEmpty()) 11 else musculoSuperiorIzquierdo.toInt()

    fun getMusculoSuperiorDerechoInt() =
        if (musculoSuperiorDerecho.isEmpty()) 11 else musculoSuperiorDerecho.toInt()

    fun getMusculoInferiorIzquierdoInt() =
        if (musculoInferiorIzquierdo.isEmpty()) 11 else musculoInferiorIzquierdo.toInt()

    fun getMusculoInferiorDerechoInt() =
        if (musculoInferiorDerecho.isEmpty()) 11 else musculoInferiorDerecho.toInt()

    fun getTroncoIzquierdoInt() = if (troncoIzquierdo.isEmpty()) 11 else troncoIzquierdo.toInt()
    fun getTroncoDerechoInt() = if (troncoDerecho.isEmpty()) 11 else troncoDerecho.toInt()
    fun getCuelloIzquierdoInt() = if (cuelloIzquierdo.isEmpty()) 11 else cuelloIzquierdo.toInt()
    fun getCuelloDerechoInt() = if (cuelloDerecho.isEmpty()) 11 else cuelloDerecho.toInt()

    companion object {
        /**
         * Method used by get data of the layout
         * @param list as a list of views and contains Evaluacion de postura data
         */
        fun FragmentMedicalConsultationBinding.getConsultationTemporary(
            list: List<SectionGradosObservacionesBinding>,
            consultationTemporary: ConsultationTemporary
        ): ConsultationTemporary {
            val listConsultation = mutableListOf<EvaluacionPosturaTemporary>()
            consultationTemporary.listOfGrados.clear()
            listConsultation.clear()
            for (i in list.indices) {
                val sectionBinding = list[i]
                val evaluacionPosturaTemporary = EvaluacionPosturaTemporary()
                evaluacionPosturaTemporary.grados = sectionBinding.layoutGrados.getText()
                evaluacionPosturaTemporary.observaciones =
                    sectionBinding.layoutObservaciones.getText()
                evaluacionPosturaTemporary.position = i
                evaluacionPosturaTemporary.name = Constants.names[i]

                if (evaluacionPosturaTemporary.isNotEmpty())
                    listConsultation.add(evaluacionPosturaTemporary)
            }
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
            consultationTemporary.valoracionInicial = layoutValoracion.getText()
            consultationTemporary.subjetivo = layoutSubjetivo.getText()
            consultationTemporary.analisis = layoutAnalisis.getText()
            consultationTemporary.planAccion = layoutPlan.getText()
            consultationTemporary.inicioMarcha = checkStart.isChecked
            consultationTemporary.pieDerechoNoSobrepasa = checkPieDerechoSobrepasa.isChecked
            consultationTemporary.pieDerechoNoLevanta = checkPieDerechoSobrepasa.isChecked
            consultationTemporary.pieIzquierdoNoSobrepasa = checkPieIzquierdoSobrepasa.isChecked
            consultationTemporary.pieIzquierdoNoLevanta =
                checkPieIzquierdoLevantamiento.isChecked
            consultationTemporary.longitud = checkLogitudPaso.isChecked
            consultationTemporary.continuidad = checkContinuidadPaso.isChecked
            consultationTemporary.trayectoriaDesviacionAlta =
                checkTayectoriaDesviacionMarcada.isChecked
            consultationTemporary.trayectoriaDesviacionMedia =
                checkTrayectoriaDesviacionModerada.isChecked
            consultationTemporary.trayectoriaDesviacionNula =
                checkTayectoriaDesviacion.isChecked
            consultationTemporary.noBalanceoAlto = checkTroncoBalanceoAyuda.isChecked
            consultationTemporary.noBalanceoMedio = checkTroncoFlexion.isChecked
            consultationTemporary.noBalanceoNulo = checkTroncoNada.isChecked
            consultationTemporary.talones = checkTalonesSeparados.isChecked
            consultationTemporary.listOfGrados.addAll(listConsultation)
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

        fun FragmentMedicalConsultationBinding.isValid(): Boolean {
            return (
                    this.layoutMotivo.isNotEmpty("Este campo es requerido.") &&
                            this.layoutSintomatologia.isNotEmpty("Este campo es requerido.") &&
                            this.layoutPesoK.isNotEmpty("Este campo es requerido.") &&
                            this.layoutPesoG.isNotEmpty("Este campo es requerido.") &&
                            this.layoutEstaturaM.isNotEmpty("Este campo es requerido.") &&
                            this.layoutEstaturaCm.isNotEmpty("Este campo es requerido.") &&
                            this.layoutTalla.isNotEmpty("Este campo es requerido.") &&
                            this.layoutReflejos.isNotEmpty("Este campo es requerido.") &&
                            this.layoutSensibilidad.isNotEmpty("Este campo es requerido.") &&
                            this.layoutLenguaje.isNotEmpty("Este campo es requerido.") &&
                            this.layoutObjetivos.isNotEmpty("Este campo es requerido.") &&
                            this.layoutHipotesis.isNotEmpty("Este campo es requerido.") &&
                            this.layoutEstructuraCorporal.isNotEmpty("Este campo es requerido.") &&
                            this.layoutFunsionCorporal.isNotEmpty("Este campo es requerido.") &&
                            this.layoutActividad.isNotEmpty("Este campo es requerido.") &&
                            this.layoutParticipacion.isNotEmpty("Este campo es requerido.") &&
                            this.layoutDiagnostico.isNotEmpty("Este campo es requerido.") &&
                            this.layoutPlan.isNotEmpty("Este campo es requerido.")
                    )
        }
    }

}
