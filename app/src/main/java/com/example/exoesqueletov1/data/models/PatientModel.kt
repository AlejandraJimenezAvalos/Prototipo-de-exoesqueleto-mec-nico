package com.example.exoesqueletov1.data.models

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants.ADDRESS
import com.example.exoesqueletov1.utils.Constants.BACKGROUND
import com.example.exoesqueletov1.utils.Constants.EMAIL
import com.example.exoesqueletov1.utils.Constants.FAMILY_BACKGROUND
import com.example.exoesqueletov1.utils.Constants.GENDER
import com.example.exoesqueletov1.utils.Constants.ID_USER
import com.example.exoesqueletov1.utils.Constants.NAME
import com.example.exoesqueletov1.utils.Constants.OCCUPATION
import com.example.exoesqueletov1.utils.Constants.ORIGIN
import com.example.exoesqueletov1.utils.Constants.PHONE
import com.example.exoesqueletov1.utils.Constants.YEARS_OLD
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "patient")
@Parcelize
data class PatientModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = YEARS_OLD) val yearsOld: String,
    @ColumnInfo(name = GENDER) val gender: String,
    @ColumnInfo(name = EMAIL) val email: String,
    @ColumnInfo(name = ADDRESS) val address: String,
    @ColumnInfo(name = PHONE) val phone: String,
    @ColumnInfo(name = ID_USER) val idUser: String,
    @ColumnInfo(name = OCCUPATION, defaultValue = "") val occupation: String,
    @ColumnInfo(name = BACKGROUND, defaultValue = "") val background: String,
    @ColumnInfo(name = FAMILY_BACKGROUND, defaultValue = "") val familyBackground: String,
    @ColumnInfo(name = ORIGIN, defaultValue = "") val origin: String
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toPatientModel(): PatientModel? = try {
            PatientModel(
                id,
                getString(NAME)!!,
                getString(YEARS_OLD)!!,
                getString(GENDER)!!,
                getString(EMAIL)!!,
                getString(ADDRESS)!!,
                getString(PHONE)!!,
                getString(ID_USER)!!,
                getString(OCCUPATION)!!,
                getString(BACKGROUND)!!,
                getString(FAMILY_BACKGROUND)!!,
                getString(ORIGIN)!!,
            )
        } catch (e: Exception) {
            Log.e("Error_to_patient", "Error to convert patient", e)
            null
        }
    }
}