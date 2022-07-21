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
@Entity(tableName = "analisis")
data class Analisis(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_MARCHA) val idMarcha: String,
    @ColumnInfo(name = Constants.ZONA) val zona: String,
    @ColumnInfo(name = Constants.VALOR) val valor: Boolean,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.getAnalisis() = try {
            Analisis(
                id,
                getString(Constants.ID_MARCHA)!!,
                getString(Constants.ZONA)!!,
                getBoolean(Constants.VALOR)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_getAnalisis", "Error to convert getAnalisis", e)
            null
        }
    }
}
