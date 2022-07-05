package com.example.exoesqueletov1.utils

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.fragment.app.FragmentManager
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.databinding.FragmentProfileBinding
import com.example.exoesqueletov1.databinding.FragmentUserBinding
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

private const val PASSWORD_VALIDATION =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@._!%*?&])([A-Za-z\\d$@._!%*?&]|[^ ]){8,15}$"
private const val ERROR_BLANK = "No puede quedarse en blanco."

object Utils {
    fun String.getTypeUser() = when (this) {
        Constants.TypeUser.Admin.toString() -> Constants.TypeUser.Admin
        Constants.TypeUser.Specialist.toString() -> Constants.TypeUser.Specialist
        Constants.TypeUser.Patient.toString() -> Constants.TypeUser.Patient
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

    fun TextInputLayout.setText(s: String) {
        editText!!.setText(s)
    }

    private fun AutoCompleteTextView.getTextIn() = text.toString().trim()

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
        if (!this.isChecked) Constants.TypeUser.Specialist.toString()
        else Constants.TypeUser.Patient.toString()

    fun RadioButton.getGender() =
        if (this.isChecked) Constants.Gender.Woman.toString()
        else Constants.Gender.Man.toString()

    fun FragmentProfileBinding.getProfileBinding(profile: (ProfileModel) -> Unit) {
        val id = Firebase.auth.currentUser!!.uid
        val name = "${user!!.name} ${user!!.lastName}"
        val addressState = layoutAddress.isNotEmpty(ERROR_BLANK)
        val cellState = layoutCellPhone.isNotEmpty(ERROR_BLANK)
        val emailState =
            layoutEmailProfile.isNotEmpty(ERROR_BLANK) && layoutEmailProfile.isValidEmail()
        val phoneState = layoutPhone.isNotEmpty(ERROR_BLANK)
        val schoolState = layoutSchool.isNotEmpty(ERROR_BLANK)
        val descriptionState = layoutDescription.isNotEmpty(ERROR_BLANK)

        if (addressState && cellState && emailState && phoneState && schoolState && descriptionState) {
            val profileModel = ProfileModel(
                id,
                layoutAddress.getText(),
                layoutCellPhone.getText(),
                layoutEmailProfile.getText(),
                name,
                layoutPhone.getText(),
                layoutSchool.getText(),
                layoutDescription.getText(),
            )
            profile.invoke(profileModel)
        }
    }

    fun ProfileModel.isEquals(profileModel: ProfileModel?, state: (Boolean) -> Unit) {
        if (profileModel == null) state.invoke(false)
        else {
            val idState = (id == profileModel.id)
            val addressState = (address == profileModel.address)
            val cellState = (cell == profileModel.cell)
            val emailState = (email == profileModel.email)
            val nameState = (name == profileModel.name)
            val phoneState = (phone == profileModel.phone)
            val schoolState = (school == profileModel.school)
            val descriptionState = (description == profileModel.description)
            state.invoke(idState && addressState && cellState && emailState && nameState && phoneState && schoolState && descriptionState)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun Date.toDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(this)
    }

    fun TextInputLayout.isValidPhone(lada: TextInputLayout): Boolean {
        val phoneValidation =
            Pattern.compile(
                "^\\s*(?:\\+?(\\d{1,3}))?" +
                        "[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*\$"
            )
        val phone = "${lada.getText()}${this.getText()}"
        return if (phone.isEmpty()) {
            this.error = null
            true
        } else {
            if (phoneValidation.matcher(phone).matches()) {
                this.error = null
                true
            } else {
                this.error = "No es un telefono valido"
                false
            }
        }
    }

    fun String.getGender() =
        if (this == Constants.Gender.Woman.toString()) Constants.Gender.Woman
        else Constants.Gender.Man

    fun String.getOrigin() =
        if (this == Constants.Origin.Create.toString()) Constants.Origin.Create
        else Constants.Origin.User

}