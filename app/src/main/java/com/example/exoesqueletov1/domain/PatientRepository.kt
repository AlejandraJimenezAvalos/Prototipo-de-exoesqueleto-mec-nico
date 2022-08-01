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

    fun setConsultationId(id: String) = id.setString(Constants.ID_CONSULT)

    fun getConsultationId() = getString(Constants.ID_CONSULT)

    fun getPatient(patient: (String, String) -> Unit) {
        patient.invoke(getString(Constants.ID), getString(Constants.NAME))
    }

    fun getId() = getString(Constants.ID)

    fun removePatient() {
        "".setString(Constants.ID)
        "".setString(Constants.NAME)
    }

    fun saveRutina(idRutina: String) = idRutina.setString(Constants.ID_RUTINA)

    fun getRutina() = getString(Constants.ID_RUTINA)


    private fun String.setString(key: String) = editor.putString(key, this).apply()
    private fun getString(key: String) = sharedPreferences.getString(key, "")!!
}