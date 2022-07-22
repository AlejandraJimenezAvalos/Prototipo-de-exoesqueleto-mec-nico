package com.example.exoesqueletov1.data.models.consultation

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "evaluacion_musculo")
data class EvaluacionMusculo(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_EVALUACION_MUSCULAR) val idEvaluacionMuscular: String,
    @ColumnInfo(name = Constants.ZONA) val zona: String,
    @ColumnInfo(name = Constants.VALOR) val valor: Int,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getEvaluacionMusculo() = try {
            EvaluacionMusculo(
                id,
                getString(Constants.ID_EVALUACION_MUSCULAR)!!,
                getString(Constants.ZONA)!!,
                getField<Int>(Constants.VALOR)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_getEvaluacionMusculo", "Error to convert EvaluacionMusculo", e)
            null
        }
    }
}
