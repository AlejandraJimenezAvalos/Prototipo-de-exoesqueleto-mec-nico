package com.example.exoesqueletov1.ui.activity.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    val result = MediatorLiveData<Resource<UserModel>>()
    val userModel: MediatorLiveData<UserModel>

    init {
        firebaseService.getUser(id) {
            if (it.status == Constants.Status.Success) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertUser(it.data!!)
                    }
                }
                result.addSource(dataRepository.getUsers(id)) { user ->
                    user.name = "${it.data!!.name} ${it.data.lastName}"
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            dataRepository.insertNewUser(user)
                        }
                    }
                }
            }
            result.postValue(it)
        }
        firebaseService.getProfile(id) {
            if (it.status == Constants.Status.Success)
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertProfile(it.data!!)
                    }
                }
            if (it.status == Constants.Status.Failure)
                result.postValue(Resource.error(it.exception!!))
        }
        userModel = MediatorLiveData<UserModel>().apply {
            addSource(dataRepository.getUser(id)) {
                if (it != null) {
                    value = it
                }
            }
        }

        userModel.addSource(dataRepository.getProfile(id)) {
            if (it != null) {
                firebaseService.setProfile(it) { resource ->
                    if (resource.status == Constants.Status.Failure)
                        result.postValue(Resource.error(resource.exception!!))
                }
            }
        }
    }

    fun singOut() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.singOut()
            }
        }
        Firebase.auth.signOut()
    }
}