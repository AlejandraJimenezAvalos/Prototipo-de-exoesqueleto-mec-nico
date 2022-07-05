package com.example.exoesqueletov1.data.models

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants.ID_PATINT
import com.example.exoesqueletov1.utils.Constants.ID_USER
import com.example.exoesqueletov1.utils.Constants.NAME
import com.example.exoesqueletov1.utils.Constants.TYPE
import com.example.exoesqueletov1.utils.Constants.VALUE
import com.google.firebase.firestore.DocumentSnapshot

@Entity(tableName = "expedient")
data class ExpedientModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = VALUE) val value: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = ID_PATINT) val idPatient: String,
    @ColumnInfo(name = TYPE) val type: String,
    @ColumnInfo(name = ID_USER) val idUser: String,
) {
    companion object {
        fun DocumentSnapshot.toExpedientModel() = try {
            ExpedientModel(
                id,
                getString(VALUE)!!,
                getString(NAME)!!,
                getString(ID_PATINT)!!,
                getString(TYPE)!!,
                getString(ID_USER)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_expedient", "Error to convert expedient", e)
            null
        }
    }
}