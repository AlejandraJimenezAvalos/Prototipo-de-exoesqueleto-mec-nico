package com.example.exoesqueletov1.ui.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseService: FirebaseService,
    private val dataRepository: DataRepository
) : ViewModel() {

    private val id = FirebaseAuth.getInstance().currentUser!!.uid

    fun getUser(resource: (Resource<UserModel>) -> Unit) {
        firebaseService.getUser(id) {
            resource.invoke(it)
            if (it.status == Constants.Status.Success) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertUser(it.data!!)
                    }
                }
            }
        }
    }
}