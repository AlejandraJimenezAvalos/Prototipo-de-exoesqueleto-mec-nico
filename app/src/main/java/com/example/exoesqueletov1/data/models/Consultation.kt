package com.example.exoesqueletov1.data.models

import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.data.models.consultation.*
import java.util.*

class Consultation {
    lateinit var patientModel: PatientModel
    lateinit var consultationData: ConsultationData
    lateinit var exploracionFisica: ExploracionFisica
    lateinit var listEvaluacionPostura: List<EvaluacionPostura>
    lateinit var diagnostico: Diagnostico
    lateinit var evaluacionMuscular: EvaluacionMuscular
    lateinit var listEvaluacionMusculo: List<EvaluacionMusculo>
    lateinit var marcha: Marcha
    lateinit var listAnalisis: List<Analisis>
    lateinit var valoracionFuncional: ValoracionFuncional
    lateinit var plan: Plan

    companion object {
        fun ConsultationTemporary.getConsultation(): Consultation {
            val idConsultation = UUID.randomUUID().toString()
            val consultation = Consultation()
            val listEvaluacionMusculo = mutableListOf<EvaluacionMusculo>()
            // region save: ConsultationData
            val consultationData = ConsultationData(
                idConsultation,
                idPatient,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                motivo,
                sintomatologia,
                dolor,
            )
            consultation.consultationData = consultationData
            // endregion
            // region save: ExploracionFisica
            consultation.exploracionFisica = ExploracionFisica(
                consultationData.idExploracion,
                idConsultation,
                pesoKg.toInt(),
                pesoG.toInt(),
                estaturaM.toInt(),
                estaturaCm.toInt(),
                talla,
            )
            // endregion
            // region save: Diagnostico
            consultation.diagnostico = Diagnostico(
                consultationData.idDiagnostico,
                idConsultation,
                reflejos,
                sensibilidad,
                lenguaje,
                otros,
            )
            //endregion
            // region save: EvaluacionMuscular
            consultation.evaluacionMuscular = EvaluacionMuscular(
                consultationData.idEvaluacionMuscular,
                idConsultation,
                valoracionInicial,
                subjetivo,
                analisis,
                planAccion,
            )
            // endregion
            // region save: Marcha
            consultation.marcha = Marcha(
                consultationData.idMarcha,
                idConsultation,
                libre,
                claudicante,
                conAyuda,
                espasticas,
                ataxica,
                observaciones,
            )
            // endregion
            // region save: ValoracionFuncional
            consultation.valoracionFuncional = ValoracionFuncional(
                consultationData.idValoracionFuncional,
                idConsultation,
                pruebasEquilibrioA.toString(),
                pruebasEquilibrioB.toString(),
                pruebasEquilibrioC.toString(),
                segundosMenor10,
                segundos.toInt(),
            )
            // endregion
            // region save: Plan
            consultation.plan = Plan(
                consultationData.idPlan,
                idConsultation,
                objetivos,
                hipotesis,
                estructuraCorporal,
                funcionCorporal,
                actividad,
                participacion,
                diagnostico,
                plan,
            )
            //endregion
            // region save: List EvaluacionMusculo
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Musculo superior izquierdo",
                    musculoSuperiorIzquierdo.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Musculo superior derecho",
                    musculoSuperiorDerecho.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Musculo inferior izquierdo",
                    musculoInferiorIzquierdo.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Musculo inferior derecho",
                    musculoInferiorDerecho.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Tronco izquierdo",
                    troncoIzquierdo.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Tronco derecho",
                    troncoDerecho.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Cuello izquierdo",
                    cuelloIzquierdo.toInt()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    consultationData.idEvaluacionMuscular,
                    "Cuello derecho",
                    cuelloDerecho.toInt()
                )
            )
            consultation.listEvaluacionMusculo = listEvaluacionMusculo
            // endregion
            // region save: List EvaluacionPostura
            val listEvaluacionPostura = mutableListOf<EvaluacionPostura>()
            listOfGrados.forEach {
                listEvaluacionPostura.add(
                    EvaluacionPostura(
                        UUID.randomUUID().toString(),
                        consultationData.idEvaluacionMuscular,
                        it.name,
                        it.grados.toInt(),
                        it.observaciones,
                    )
                )
            }
            consultation.listEvaluacionPostura = listEvaluacionPostura
            // endregion
            // region save: List Analisis
            val listAnalisis = mutableListOf<Analisis>()
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Duda o vacila o múltiples intentos para comenzar.",
                    inicioMarcha
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "El pie derecho no sobrepasaal izquierdo con el paso en la fase balanceo.",
                    pieDerechoNoSobrepasa
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "El pie derecho no se levanta completamente del suelo con el paso en la fase de balanceo.",
                    pieDerechoNoLevanta
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "El pie izquierdo no sobrepasa al derecho con el paso en la fase de balanceo.",
                    pieIzquierdoNoSobrepasa
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "El pie izquierdo no se levanta completamente del suelo con el paso en la fase de balanceo.",
                    pieIzquierdoNoLevanta
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "La longitud del paso con el pie derecho e izquierdo es diferente (estimada).",
                    longitud
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Para, o hay descontinuidad entre los pasos.",
                    continuidad
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Marcado desviación",
                    trayectoriaDesviacionAlta
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Desviación modelada, media o utiliza ayudas.",
                    trayectoriaDesviacionMedia
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Derecho sin utilizar ayudas.",
                    trayectoriaDesviacionNula
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Marcado balanceo o utiliza ayudas",
                    noBalanceoAlto
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "No balancea, pero hay flexión de rodillas, espalda o extención hacia afuera de los brazos",
                    noBalanceoMedio
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "No balanceo ni flexión y tampoco utiliza ayudas.",
                    noBalanceoNulo
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    consultationData.idMarcha,
                    "Talones separados.",
                    talones
                )
            )
            consultation.listAnalisis = listAnalisis
            // endregion
            return consultation
        }
    }
}