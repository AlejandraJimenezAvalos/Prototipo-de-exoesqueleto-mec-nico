package com.example.exoesqueletov1.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMuscular
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMusculo

data class EvaluacionMuscularWithMusculo(
    @Embedded val evaluacionMuscular: EvaluacionMuscular?,
    @Relation(
        parentColumn = "id",
        entityColumn = "idEvaluacionMuscular",
    )
    val evaluacionMusculo: List<EvaluacionMusculo>?
)
