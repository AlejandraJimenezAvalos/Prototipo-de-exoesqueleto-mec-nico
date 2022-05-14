package com.example.exoesqueletov1.data.utils

import android.util.Patterns
import com.example.exoesqueletov1.data.firebase.Constants
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

private const val Re =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@._$!%*?&])([A-Za-z\\d$@._$!%*?&]|[^ ]){8,15}$"

object Utils {
    fun String.getTypeUser() = when (this) {
        "a" -> Constants.TypeUser.Admin
        "b" -> Constants.TypeUser.B
        "c" -> Constants.TypeUser.C
        else -> Constants.TypeUser.C
    }

    fun TextInputLayout.isNotEmpty(e: String) =
        if (this.getText().isNotEmpty()) {
            error = null
            true
        } else {
            error = e
            false
        }

    fun TextInputLayout.isValidEmail(): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return if (pattern.matcher(this.getText()).matches()) {
            error = null
            true
        } else {
            error = "No es un correo valido"
            false
        }
    }

    fun TextInputLayout.isValidPassword(): Boolean {
        val pattern = Pattern.compile(
            Re,
            Pattern.MULTILINE
        )
        return if (pattern.matcher(this.getText()).matches()) {
            error = null
            true
        } else {
            error = "Contraseña invalida"
            false
        }
    }

    fun TextInputLayout.getText() = editText!!.text.toString().trim()
}