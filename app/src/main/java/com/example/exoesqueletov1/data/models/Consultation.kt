package com.example.exoesqueletov1.data.models

import com.example.exoesqueletov1.data.models.consultation.*

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
}