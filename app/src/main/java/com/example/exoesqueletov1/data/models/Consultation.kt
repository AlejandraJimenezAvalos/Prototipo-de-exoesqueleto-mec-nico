package com.example.exoesqueletov1.data.models

import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.data.models.consultation.*
import com.example.exoesqueletov1.utils.Utils.toDate
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
            val idExploracion = UUID.randomUUID().toString()
            val idMarcha = UUID.randomUUID().toString()
            val idDiagnostico = UUID.randomUUID().toString()
            val idEvaluacionMuscular = UUID.randomUUID().toString()
            val idValoracionFuncional = UUID.randomUUID().toString()
            val idPlan = UUID.randomUUID().toString()
            // region save: ConsultationData
            val consultationData = ConsultationData(
                idConsultation,
                idPatient,
                idUser,
                motivo,
                sintomatologia,
                dolor,
                Date().toDate(),
            )
            consultation.consultationData = consultationData
            // endregion                                                                 
            // region save: ExploracionFisica                                            
            consultation.exploracionFisica = ExploracionFisica(
                idExploracion,
                idConsultation,
                pesoKg.toInt(),
                pesoG.toInt(),
                estaturaM.toInt(),
                estaturaCm.toInt(),
                talla,
                Date().toDate(),
            )
            // endregion
            // region save: Diagnostico
            consultation.diagnostico = Diagnostico(
                idDiagnostico,
                idConsultation,
                reflejos,
                sensibilidad,
                lenguaje,
                otros,
                Date().toDate(),
            )
            //endregion
            // region save: EvaluacionMuscular
            consultation.evaluacionMuscular = EvaluacionMuscular(
                idEvaluacionMuscular,
                idConsultation,
                valoracionInicial,
                subjetivo,
                analisis,
                planAccion,
                Date().toDate(),
            )
            // endregion
            // region save: Marcha
            consultation.marcha = Marcha(
                idMarcha,
                idConsultation,
                libre,
                claudicante,
                conAyuda,
                espasticas,
                ataxica,
                observaciones,
                Date().toDate(),
            )
            // endregion
            // region save: ValoracionFuncional
            consultation.valoracionFuncional = ValoracionFuncional(
                idValoracionFuncional,
                idConsultation,
                pruebasEquilibrioA.toString(),
                pruebasEquilibrioB.toString(),
                pruebasEquilibrioC.toString(),
                segundosMenor10,
                segundos.toInt(),
                Date().toDate(),
            )
            // endregion
            // region save: Plan
            consultation.plan = Plan(
                idPlan,
                idConsultation,
                objetivos,
                hipotesis,
                estructuraCorporal,
                funcionCorporal,
                actividad,
                participacion,
                diagnostico,
                plan,
                Date().toDate(),
            )
            //endregion
            // region save: List EvaluacionMusculo
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Musculo superior izquierdo",
                    musculoSuperiorIzquierdo.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Musculo superior derecho",
                    musculoSuperiorDerecho.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Musculo inferior izquierdo",
                    musculoInferiorIzquierdo.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Musculo inferior derecho",
                    musculoInferiorDerecho.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Tronco izquierdo",
                    troncoIzquierdo.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Tronco derecho",
                    troncoDerecho.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Cuello izquierdo",
                    cuelloIzquierdo.toInt(),
                    Date().toDate()
                )
            )
            listEvaluacionMusculo.add(
                EvaluacionMusculo(
                    UUID.randomUUID().toString(),
                    idEvaluacionMuscular,
                    "Cuello derecho",
                    cuelloDerecho.toInt(),
                    Date().toDate()
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
                        idConsultation,
                        it.name,
                        if (it.grados.isNotEmpty()) it.grados.toInt() else 0,
                        it.observaciones,
                        Date().toDate(),
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
                    idMarcha,
                    "Duda o vacila o múltiples intentos para comenzar.",
                    inicioMarcha,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "El pie derecho no sobrepasaal izquierdo con el paso en la fase balanceo.",
                    pieDerechoNoSobrepasa,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "El pie derecho no se levanta completamente del suelo con el paso en la fase de balanceo.",
                    pieDerechoNoLevanta,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "El pie izquierdo no sobrepasa al derecho con el paso en la fase de balanceo.",
                    pieIzquierdoNoSobrepasa,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "El pie izquierdo no se levanta completamente del suelo con el paso en la fase de balanceo.",
                    pieIzquierdoNoLevanta,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "La longitud del paso con el pie derecho e izquierdo es diferente (estimada).",
                    longitud,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "Para, o hay descontinuidad entre los pasos.",
                    continuidad,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "Marcado desviación",
                    trayectoriaDesviacionAlta,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "Desviación modelada, media o utiliza ayudas.",
                    trayectoriaDesviacionMedia,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "Derecho sin utilizar ayudas.",
                    trayectoriaDesviacionNula,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "Marcado balanceo o utiliza ayudas",
                    noBalanceoAlto,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "No balancea, pero hay flexión de rodillas, espalda o extención hacia afuera de los brazos",
                    noBalanceoMedio,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "No balanceo ni flexión y tampoco utiliza ayudas.",
                    noBalanceoNulo,
                    Date().toDate(),
                )
            )
            listAnalisis.add(
                Analisis(
                    UUID.randomUUID().toString(),
                    idMarcha,
                    "Talones separados.",
                    talones,
                    Date().toDate(),
                )
            )
            consultation.listAnalisis = listAnalisis
            // endregion
            return consultation
        }
    }
}// 2022-07-22 17:05:41