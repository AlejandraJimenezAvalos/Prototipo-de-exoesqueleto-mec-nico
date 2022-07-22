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
@Entity(tableName = "marcha")
data class Marcha(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.LIBRE) val libre: Boolean,
    @ColumnInfo(name = Constants.CLAUDANTE) val claudante: Boolean,
    @ColumnInfo(name = Constants.CON_AYUDA) val conAyuda: Boolean,
    @ColumnInfo(name = Constants.ESPATICAS) val espaticas: Boolean,
    @ColumnInfo(name = Constants.ATAXICA) val ataxica: Boolean,
    @ColumnInfo(name = Constants.OBSERVACIONES) val observaciones: String,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getMarcha() = try {
            Marcha(
                id,
                getString(Constants.ID_CONSULT)!!,
                getBoolean(Constants.LIBRE)!!,
                getBoolean(Constants.CLAUDANTE)!!,
                getBoolean(Constants.CON_AYUDA)!!,
                getBoolean(Constants.ESPATICAS)!!,
                getBoolean(Constants.ATAXICA)!!,
                getString(Constants.OBSERVACIONES)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_getMarcha", "Error to convert getMarcha", e)
            null
        }
    }
}