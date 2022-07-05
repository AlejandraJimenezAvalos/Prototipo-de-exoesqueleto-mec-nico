package com.example.exoesqueletov1.data.models

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants.ADDRESS
import com.example.exoesqueletov1.utils.Constants.BIRTHDAY
import com.example.exoesqueletov1.utils.Constants.EMAIL
import com.example.exoesqueletov1.utils.Constants.GENDER
import com.example.exoesqueletov1.utils.Constants.ID_USER
import com.example.exoesqueletov1.utils.Constants.LADA
import com.example.exoesqueletov1.utils.Constants.NAME
import com.example.exoesqueletov1.utils.Constants.OCCUPATION
import com.example.exoesqueletov1.utils.Constants.ORIGIN
import com.example.exoesqueletov1.utils.Constants.PHONE
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "patient")
@Parcelize
data class PatientModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = BIRTHDAY, defaultValue = "") val birthday: String,
    @ColumnInfo(name = GENDER) val gender: String,
    @ColumnInfo(name = EMAIL) val email: String,
    @ColumnInfo(name = ADDRESS) val address: String,
    @ColumnInfo(name = PHONE) val phone: String,
    @ColumnInfo(name = LADA, defaultValue = "") val lada: String,
    @ColumnInfo(name = ID_USER) val idUser: String,
    @ColumnInfo(name = OCCUPATION, defaultValue = "") val occupation: String,
    @ColumnInfo(name = ORIGIN, defaultValue = "") val origin: String
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toPatientModel(): PatientModel? = try {
            PatientModel(
                id,
                getString(NAME)!!,
                getString(BIRTHDAY)!!,
                getString(GENDER)!!,
                getString(EMAIL)!!,
                getString(ADDRESS)!!,
                getString(PHONE)!!,
                getString(LADA)!!,
                getString(ID_USER)!!,
                getString(OCCUPATION)!!,
                getString(ORIGIN)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_patient", "Error to convert patient", e)
            null
        }
    }
}