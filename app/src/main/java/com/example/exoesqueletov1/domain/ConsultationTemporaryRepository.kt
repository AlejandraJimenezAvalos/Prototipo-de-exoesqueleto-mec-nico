package com.example.exoesqueletov1.domain

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named

class ConsultationTemporaryRepository @Inject constructor(@Named("consultationTemporary") private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    private fun String.setString(key: String) = editor.putString(key, this).apply()
    private fun getString(key: String) = sharedPreferences.getString(key, "")!!
}