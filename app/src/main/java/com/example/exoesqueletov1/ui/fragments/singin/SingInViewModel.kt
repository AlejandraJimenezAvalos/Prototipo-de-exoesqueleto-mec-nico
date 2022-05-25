package com.example.exoesqueletov1.ui.fragments.singin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.auth.FirebaseAuth

class SingInViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun singUp(email: String, password: String): MutableLiveData<Resource<String>> {
        val resource = MutableLiveData<Resource<String>>()
        resource.postValue(Resource.loading())
        auth.createUserWithEmailAndPassword(email, password).addOnFailureListener {
            resource.postValue(Resource.error(it))
        }.addOnCanceledListener {
            Resource.canceled<String>()
        }
        return resource
    }
}