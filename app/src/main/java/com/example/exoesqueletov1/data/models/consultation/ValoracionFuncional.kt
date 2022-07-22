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
@Entity(tableName = "valoracion_funcional")
data class ValoracionFuncional(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.RESPUESTA_A) val respuestaA: String,
    @ColumnInfo(name = Constants.RESPUESTA_B) val respuestaB: String,
    @ColumnInfo(name = Constants.RESPUESTA_C) val respuestaC: String,
    @ColumnInfo(name = Constants.STATE_SEGUNDOS) val stateSegundos: Boolean,
    @ColumnInfo(name = Constants.SEGUNDOS) val segundos: Int,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getValoracionFuncional() = try {
            ValoracionFuncional(
                id,
                getString(Constants.ID_CONSULT)!!,
                getString(Constants.RESPUESTA_A)!!,
                getString(Constants.RESPUESTA_B)!!,
                getString(Constants.RESPUESTA_C)!!,
                getBoolean(Constants.STATE_SEGUNDOS)!!,
                getField<Int>(Constants.SEGUNDOS)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e(
                "Error_to_getValoracionFuncional",
                "Error to convert getValoracionFuncional",
                e
            )
            null
        }
    }
}