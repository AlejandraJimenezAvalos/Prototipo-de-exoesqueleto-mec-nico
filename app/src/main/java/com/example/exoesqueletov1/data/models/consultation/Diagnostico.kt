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
@Entity(tableName = "diagnostico")
data class Diagnostico(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.REFLEJO) val reflejos: String,
    @ColumnInfo(name = Constants.SENSIBIDAD) val sensibilidad: String,
    @ColumnInfo(name = Constants.LENGUAJE) val lenguaje: String,
    @ColumnInfo(name = Constants.OTROS) val otros: String,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getDiagnostico() = try {
            Diagnostico(
                id,
                getString(Constants.ID_CONSULT)!!,
                getString(Constants.REFLEJO)!!,
                getString(Constants.SENSIBIDAD)!!,
                getString(Constants.LENGUAJE)!!,
                getString(Constants.OTROS)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_getDiagnostico", "Error to convert getDiagnostico", e)
            null
        }
    }
}
