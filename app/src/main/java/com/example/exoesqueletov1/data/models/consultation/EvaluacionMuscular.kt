package com.example.exoesqueletov1.data.models.consultation

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "evaluacion_muscular")
data class EvaluacionMuscular(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.VALORACION) val valoracion: String,
    @ColumnInfo(name = Constants.SUBJETIVO) val subjetivo: String,
    @ColumnInfo(name = Constants.ANALISIS) val analisis: String,
    @ColumnInfo(name = Constants.PLAN_ACCION) val planAccion: String,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getEvaluacionMuscular() = try {
            EvaluacionMuscular(
                id,
                getString(Constants.ID_CONSULT)!!,
                getString(Constants.VALORACION)!!,
                getString(Constants.SUBJETIVO)!!,
                getString(Constants.ANALISIS)!!,
                getString(Constants.PLAN_ACCION)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_getEvaluacionMuscular", "Error to convert EvaluacionMuscular", e)
            null
        }
    }
}