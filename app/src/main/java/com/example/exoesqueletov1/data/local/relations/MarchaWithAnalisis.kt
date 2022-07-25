package com.example.exoesqueletov1.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.exoesqueletov1.data.models.consultation.Analisis
import com.example.exoesqueletov1.data.models.consultation.Marcha

data class MarchaWithAnalisis(
    @Embedded val marcha: Marcha?,
    @Relation(
        parentColumn = "id",
        entityColumn = "idMarcha",
    )
    val analisis: List<Analisis>?
)