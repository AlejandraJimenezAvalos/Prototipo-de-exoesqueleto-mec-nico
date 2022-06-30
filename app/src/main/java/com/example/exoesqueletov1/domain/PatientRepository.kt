package com.example.exoesqueletov1.domain

import android.content.SharedPreferences
import com.example.exoesqueletov1.utils.Constants
import javax.inject.Inject
import javax.inject.Named

class PatientRepository @Inject constructor(@Named("patient") private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    fun setPatient(id: String, name: String) {
        id.setString(Constants.ID)
        name.setString(Constants.NAME)
    }

    fun getPatient(patient: (String, String) -> Unit) {
        patient.invoke(getString(Constants.ID), getString(Constants.NAME))
    }

    fun removePatient() {
        "".setString(Constants.ID)
        "".setString(Constants.NAME)
    }

    private fun String.setString(key: String) = editor.putString(key, this).apply()
    private fun getString(key: String) = sharedPreferences.getString(key, "")!!
}