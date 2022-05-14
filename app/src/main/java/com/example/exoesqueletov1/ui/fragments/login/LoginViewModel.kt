package com.example.exoesqueletov1.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.firebase.models.UserModel.Companion.toUser
import com.example.exoesqueletov1.data.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseService: FirebaseService) :
    ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): MutableLiveData<Resource<String>> {
        val result = MutableLiveData<Resource<String>>()
        result.postValue(Resource.loading())

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) auth.currentUser!!.toUser()
                if (task.exception != null) result.postValue(Resource.error(task.exception!!))
                if (task.isCanceled != null) result.postValue(Resource.canceled())
            }
        return result
    }
}