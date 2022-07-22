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
@Entity(tableName = "plan")
data class Plan(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.OBJETIVOS) val objetivos: String,
    @ColumnInfo(name = Constants.HIPOTESIS) val hipotesis: String,
    @ColumnInfo(name = Constants.ESTRUCTURA) val estructura: String,
    @ColumnInfo(name = Constants.FUNSION) val funsion: String,
    @ColumnInfo(name = Constants.ACTIVIDAD) val actividad: String,
    @ColumnInfo(name = Constants.PARTICIPACION) val participacion: String,
    @ColumnInfo(name = Constants.DIAGNOSTICO) val diagnostico: String,
    @ColumnInfo(name = Constants.PLAN) val plan: String,
    @ColumnInfo(name = Constants.DATE, defaultValue = "") val date: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getPlan() = try {
            Plan(
                id,
                getString(Constants.ID_CONSULT)!!,
                getString(Constants.OBJETIVOS)!!,
                getString(Constants.HIPOTESIS)!!,
                getString(Constants.ESTRUCTURA)!!,
                getString(Constants.FUNSION)!!,
                getString(Constants.ACTIVIDAD)!!,
                getString(Constants.PARTICIPACION)!!,
                getString(Constants.DIAGNOSTICO)!!,
                getString(Constants.PLAN)!!,
                getString(Constants.DATE)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_getPlan", "Error to convert getPlan", e)
            null
        }
    }
}