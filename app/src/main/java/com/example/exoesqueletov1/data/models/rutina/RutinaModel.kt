package com.example.exoesqueletov1.data.models.rutina

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getModo
import com.example.exoesqueletov1.utils.Utils.getType
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "rutina")
data class RutinaModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.TYPE) var type: String,
    @ColumnInfo(name = Constants.MODO) var modo: String,
    @ColumnInfo(name = Constants.VALUE) var value: String,
    @ColumnInfo(name = Constants.FINALIZE) var finalize: String,
    @ColumnInfo(name = Constants.ID_PATIENT) var idPatient: String,
    @ColumnInfo(name = Constants.USER_ID) var userId: String,
) : Parcelable {

    fun isValid() =
        (type.getType() != Constants.Type.Null) && (modo.getModo() != Constants.Modo.Null) && (value.isNotEmpty())

    companion object {
        fun DocumentSnapshot.toRutinaModel() = try {
            RutinaModel(
                id,
                getString(Constants.TYPE)!!,
                getString(Constants.MODO)!!,
                getString(Constants.VALUE)!!,
                getString(Constants.FINALIZE)!!,
                getString(Constants.ID_PATIENT)!!,
                getString(Constants.USER_ID)!!
            )
        } catch (e: Exception) {
            Log.e("Error_to_rutina_model", "Error to convert RutinaModel", e)
            null
        }
    }
}