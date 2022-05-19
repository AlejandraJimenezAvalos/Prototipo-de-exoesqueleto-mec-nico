package com.example.exoesqueletov1.utils

import android.util.Patterns
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.fragment.app.FragmentManager
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.databinding.FragmentUserBinding
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.util.regex.Pattern

private const val PASSWORD_VALIDATION =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@._$!%*?&])([A-Za-z\\d$@._$!%*?&]|[^ ]){8,15}$"

object Utils {
    fun String.getTypeUser() = when (this) {
        "a" -> Constants.TypeUser.Admin
        "b" -> Constants.TypeUser.Specialist
        "c" -> Constants.TypeUser.Patient
        else -> Constants.TypeUser.Patient
    }

    fun TextInputLayout.isNotEmpty(e: String) =
        if (this.getText().isNotEmpty()) {
            error = null
            true
        } else {
            error = e
            false
        }

    fun FragmentUserBinding.getUser(): UserModel {
        val userFirebase = FirebaseAuth.getInstance().currentUser!!
        return UserModel(
            userFirebase.uid,
            userFirebase.email!!,
            spinnerCountry.getTextIn(),
            editTextDate.getText(),
            radioWomen.getGender(),
            editTextApellidos.getText(),
            editTextNombre.getText(),
            radioDoctor.getTypeUser()
        )
    }

    fun TextInputLayout.isNotEmpty(spinner: AutoCompleteTextView, e: String) =
        if (spinner.text.isNotEmpty()) {
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
            PASSWORD_VALIDATION,
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

    fun TextInputLayout.confirmPassword(layoutPassword: TextInputLayout) =
        if (this.getText() == layoutPassword.getText()) {
            error = null
            true
        } else {
            error = "Las contraseñas no coinciden"
            false
        }

    fun TextInputLayout.getText() = editText!!.text.toString().trim()

    fun AutoCompleteTextView.getTextIn() = text.toString().trim()

    fun Exception.setError(fragmentManager: FragmentManager, tag: String): DialogOops {
        val dialogOops = DialogOops(message)
        dialogOops.show(fragmentManager, tag)
        return dialogOops
    }

    fun Constants.Status.createLoadingDialog(
        fragmentManager: FragmentManager,
        tag: String,
        loading: DialogLoading,
    ) {
        if (this == Constants.Status.Loading) {
            loading.show(fragmentManager, tag)
        } else {
            loading.dismiss()
        }
    }

    fun RadioButton.getTypeUser() =
        if (this.isChecked) Constants.TypeUser.Specialist.toString()
        else Constants.TypeUser.Patient.toString()

    fun RadioButton.getGender() =
        if (this.isChecked) Constants.Gender.Woman.toString()
        else Constants.Gender.Man.toString()
}