package com.example.exoesqueletov1.domain

import android.content.SharedPreferences
import com.example.exoesqueletov1.data.local.sharedpreferences.PatientTemporary
import com.example.exoesqueletov1.utils.Constants.ADDRESS
import com.example.exoesqueletov1.utils.Constants.BIRTHDAY
import com.example.exoesqueletov1.utils.Constants.EMAIL
import com.example.exoesqueletov1.utils.Constants.GENDER
import com.example.exoesqueletov1.utils.Constants.ID_PATIENT
import com.example.exoesqueletov1.utils.Constants.LADA
import com.example.exoesqueletov1.utils.Constants.NAME
import com.example.exoesqueletov1.utils.Constants.OCCUPATION
import com.example.exoesqueletov1.utils.Constants.PHONE
import javax.inject.Inject
import javax.inject.Named

class PatientTemporaryRepository @Inject constructor(@Named("formPatient") private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    fun setPatient(patientTemporary: PatientTemporary) {
        patientTemporary.apply {
            name.setString(NAME)
            birthday.setString(BIRTHDAY)
            gender.setString(GENDER)
            occupation.setString(OCCUPATION)
            email.setString(EMAIL)
            phone.setString(PHONE)
            address.setString(ADDRESS)
            lada.setString(LADA)
            idPatient.setString(ID_PATIENT)
        }
    }

    fun getPatient(): PatientTemporary {
        val patient = PatientTemporary()
        patient.apply {
            name = getString(NAME)
            birthday = getString(BIRTHDAY)
            gender = getString(GENDER)
            occupation = getString(OCCUPATION)
            email = getString(EMAIL)
            phone = getString(PHONE)
            lada = getString(LADA)
            address = getString(ADDRESS)
            idPatient = getString(ID_PATIENT)
        }
        return patient
    }

    fun delete() {
        "".setString(NAME)
        "".setString(BIRTHDAY)
        "".setString(GENDER)
        "".setString(OCCUPATION)
        "".setString(EMAIL)
        "".setString(PHONE)
        "".setString(ADDRESS)
        "".setString(LADA)
        "".setString(ID_PATIENT)
    }

    private fun String.setString(key: String) = editor.putString(key, this).apply()
    private fun getString(key: String) = sharedPreferences.getString(key, "")!!
}