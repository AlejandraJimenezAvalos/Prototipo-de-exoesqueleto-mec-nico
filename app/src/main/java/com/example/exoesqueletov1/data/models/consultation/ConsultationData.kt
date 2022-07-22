package com.example.exoesqueletov1.data.models.consultation

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants.DATE
import com.example.exoesqueletov1.utils.Constants.DOLOR
import com.example.exoesqueletov1.utils.Constants.ID_ANALISIS
import com.example.exoesqueletov1.utils.Constants.ID_DIAGNOSTICO
import com.example.exoesqueletov1.utils.Constants.ID_EVALUACION_MUSCULAR
import com.example.exoesqueletov1.utils.Constants.ID_EVALUACION_POSTURA
import com.example.exoesqueletov1.utils.Constants.ID_EXPLORACION
import com.example.exoesqueletov1.utils.Constants.ID_MARCHA
import com.example.exoesqueletov1.utils.Constants.ID_PATIENT
import com.example.exoesqueletov1.utils.Constants.ID_PLAN
import com.example.exoesqueletov1.utils.Constants.ID_VALORACION_FUNCIONAL
import com.example.exoesqueletov1.utils.Constants.MOTIVO
import com.example.exoesqueletov1.utils.Constants.SINTOMATOLOGIA
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "consultations")
@Parcelize
data class ConsultationData(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = ID_PATIENT) var idPatient: String,
    @ColumnInfo(name = ID_EXPLORACION) val idExploracion: String,
    @ColumnInfo(name = ID_MARCHA) val idMarcha: String,
    @ColumnInfo(name = ID_DIAGNOSTICO) val idDiagnostico: String,
    @ColumnInfo(name = ID_EVALUACION_MUSCULAR) val idEvaluacionMuscular: String,
    @ColumnInfo(name = ID_ANALISIS) val idAnalisis: String,
    @ColumnInfo(name = ID_EVALUACION_POSTURA) val idEvaluacionPostura: String,
    @ColumnInfo(name = ID_VALORACION_FUNCIONAL) val idValoracionFuncional: String,
    @ColumnInfo(name = ID_PLAN) val idPlan: String,
    @ColumnInfo(name = MOTIVO) val motivo: String,
    @ColumnInfo(name = SINTOMATOLOGIA) val sintomatologia: String,
    @ColumnInfo(name = DOLOR) val dolor: String,
    @ColumnInfo(name = DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toConsultationData() =
            try {
                ConsultationData(
                    id,
                    getString(ID_PATIENT)!!,
                    getString(ID_EXPLORACION)!!,
                    getString(ID_MARCHA)!!,
                    getString(ID_DIAGNOSTICO)!!,
                    getString(ID_EVALUACION_MUSCULAR)!!,
                    getString(ID_ANALISIS)!!,
                    getString(ID_EVALUACION_POSTURA)!!,
                    getString(ID_VALORACION_FUNCIONAL)!!,
                    getString(ID_PLAN)!!,
                    getString(MOTIVO)!!,
                    getString(SINTOMATOLOGIA)!!,
                    getString(DOLOR)!!,
                    getString(DATE)!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_ConsultationData", "Error to convert ConsultationData", e)
                null
            }
    }
}
