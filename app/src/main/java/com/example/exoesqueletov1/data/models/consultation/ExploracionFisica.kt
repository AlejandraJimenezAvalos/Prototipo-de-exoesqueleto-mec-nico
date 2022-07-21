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

@Entity(tableName = "exploracion_fisica")
@Parcelize
data class ExploracionFisica(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.ID_CONSULT) val idConsult: String,
    @ColumnInfo(name = Constants.PESO_KG) val pesoKg: Int,
    @ColumnInfo(name = Constants.PESO_G) val pesoG: Int,
    @ColumnInfo(name = Constants.ESTATURA_M) val estaturaM: Int,
    @ColumnInfo(name = Constants.ESTATURA_CM) val estaturaCm: Int,
    @ColumnInfo(name = Constants.TALLA) val talla: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toExploracionFisica() = try {
            ExploracionFisica(
                id,
                getString(Constants.ID_CONSULT)!!,
                getField<Int>(Constants.PESO_KG)!!,
                getField<Int>(Constants.PESO_G)!!,
                getField<Int>(Constants.ESTATURA_M)!!,
                getField<Int>(Constants.ESTATURA_CM)!!,
                getString(Constants.TALLA)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_ExploracionFisica", "Error to convert ExploracionFisica", e)
            null
        }
    }
}
