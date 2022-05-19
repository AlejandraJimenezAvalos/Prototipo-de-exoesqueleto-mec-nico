package com.example.exoesqueletov1.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.UserModel.Companion.toUser
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): MutableLiveData<Resource<String>> {
        val result = MutableLiveData<Resource<String>>()
        result.postValue(Resource.loading())

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) auth.currentUser!!.toUser()
                if (task.exception != null) result.postValue(Resource.error(task.exception!!))
            }
        return result
    }

    fun sendEmailReset(email: String): MutableLiveData<Resource<String>> {
        val result = MutableLiveData<Resource<String>>()
        result.postValue(Resource.loading())
        val url = "https://exoesqueletov1.page.link/__/auth/action?mode=action"
        val actionCodeSetting = ActionCodeSettings.newBuilder()
            .setUrl(url)
            .setIOSBundleId("com.example.exoesqueletov1")
            .setAndroidPackageName("com.example.exoesqueletov1", false, null)
            .build()
            auth.sendPasswordResetEmail(email, actionCodeSetting).addOnSuccessListener {
                result.postValue(Resource.success(""))
            }.addOnFailureListener {
                result.postValue(Resource.error(it))
            }
        return result
    }
}