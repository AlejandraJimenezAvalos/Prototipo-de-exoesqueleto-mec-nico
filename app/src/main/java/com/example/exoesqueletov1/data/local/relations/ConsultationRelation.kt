package com.example.exoesqueletov1.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.exoesqueletov1.data.models.consultation.*

data class ConsultationRelation(
    @Embedded val consultation: ConsultationData?,
    @Relation(
        entity = Marcha::class,
        parentColumn = "id",
        entityColumn = "idConsult"
    )
    val marcha: MarchaWithAnalisis?,
    @Relation(
        entity = EvaluacionMuscular::class,
        parentColumn = "id",
        entityColumn = "idConsult"
    )
    val evaluacionMuscular: EvaluacionMuscularWithMusculo?,
    @Relation(
        entity = EvaluacionPostura::class,
        parentColumn = "id",
        entityColumn = "idConsult",
    )
    val evaluacionPostura: List<EvaluacionPostura>?,
    @Relation(
        entity = ExploracionFisica::class,
        parentColumn = "id",
        entityColumn = "idConsult",
    )
    val exploracionFisica: ExploracionFisica?,
    @Relation(
        entity = Diagnostico::class,
        parentColumn = "id",
        entityColumn = "idConsult",
    )
    val diagnostico: Diagnostico?,
    @Relation(
        entity = ValoracionFuncional::class,
        parentColumn = "id",
        entityColumn = "idConsult",
    )
    val valoracionFuncional: ValoracionFuncional?,
    @Relation(
        entity = Plan::class,
        parentColumn = "id",
        entityColumn = "idConsult",
    )
    val plan: Plan?,
)