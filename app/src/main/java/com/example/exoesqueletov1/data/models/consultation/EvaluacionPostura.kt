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

@Entity(tableName = "evaluacion_postura")
@Parcelize
data class EvaluacionPostura(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.VISTA) val vista: String,
    @ColumnInfo(name = Constants.GRADOS) val grados: Int,
    @ColumnInfo(name = Constants.OBSERVACIONES) val observaciones: String,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getEvaluacionPostura() = try {
            EvaluacionPostura(
                id,
                getString(Constants.ID_CONSULT)!!,
                getString(Constants.VISTA)!!,
                getField<Int>(Constants.GRADOS)!!,
                getString(Constants.OBSERVACIONES)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_EvaluacionPostura", "Error to convert EvaluacionPostura", e)
            null
        }
    }
}
