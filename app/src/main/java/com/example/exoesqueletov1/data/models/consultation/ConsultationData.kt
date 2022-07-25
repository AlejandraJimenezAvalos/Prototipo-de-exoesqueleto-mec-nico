package com.example.exoesqueletov1.data.models.consultation

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants.DATE
import com.example.exoesqueletov1.utils.Constants.DOLOR
import com.example.exoesqueletov1.utils.Constants.ID_PATIENT
import com.example.exoesqueletov1.utils.Constants.ID_USER
import com.example.exoesqueletov1.utils.Constants.MOTIVO
import com.example.exoesqueletov1.utils.Constants.SINTOMATOLOGIA
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "consultations")
@Parcelize
data class ConsultationData(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = ID_PATIENT) var idPatient: String,
    @ColumnInfo(name = ID_USER, defaultValue = "") var idUser: String,
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
                    getString(ID_USER)!!,
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
