package com.example.exoesqueletov1.domain

import android.content.SharedPreferences
import com.example.exoesqueletov1.utils.Constants.ID
import com.example.exoesqueletov1.utils.Constants.USER
import javax.inject.Inject
import javax.inject.Named

class UserRepository @Inject constructor(@Named("user") private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    fun setUsertype(userType: String) = userType.setString(USER)

    fun setId(id: String) = id.setString(ID)

    fun getId() = getString(ID)

    fun getType() = getString(USER)

    fun getUser(user: (String, String) -> Unit) {
        user.invoke(getString(ID), getString(USER))
    }

    fun logout() {
        "".setString(ID)
        "".setString(USER)
    }

    private fun String.setString(key: String) = editor.putString(key, this).apply()
    private fun getString(key: String) = sharedPreferences.getString(key, "")!!
}