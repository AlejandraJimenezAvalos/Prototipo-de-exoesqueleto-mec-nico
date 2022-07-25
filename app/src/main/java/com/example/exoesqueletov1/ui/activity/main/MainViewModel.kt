package com.example.exoesqueletov1.ui.activity.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.UserRepository
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Resource
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
    private val dataRepository: DataRepository,
    userRepository: UserRepository
) : ViewModel() {
    private val id = userRepository.getId()
    val result = MediatorLiveData<Resource<UserModel>>()
    val userModel: MediatorLiveData<UserModel>

    init {

        firebaseService.getUser(id) {
            if (it.status == Constants.Status.Success) {
                if (it.data!!.id == id) getExoskeleton()
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertUser(it.data)
                    }
                }
                if (it.data.id == id) {
                    userRepository.setUsertype(it.data.user)
                    result.addSource(dataRepository.getUsers(id)) { user ->
                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                user.name = "${it.data.name} ${it.data.lastName}"
                                dataRepository.insertNewUser(user)
                            }
                        }
                    }
                }
            }
            result.postValue(it)
        }

        firebaseService.getProfile {
            if (it.status == Constants.Status.Success)
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertProfile(it.data!!)
                    }
                }
            if (it.status == Constants.Status.Failure)
                result.postValue(Resource.error(it.exception!!))
        }

        firebaseService.getMessages {
            if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.status == Constants.Status.Success) dataRepository.insertMessage(it.data!!)
                }
            }
        }

        firebaseService.getPatient {
            if (it.status == Constants.Status.Success) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertPatient(it.data!!)
                    }
                }
            }
            if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
        }

        firebaseService.getExpedient {
            if (it.status == Constants.Status.Success) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.setExpedient(it.data!!)
                    }
                }
            }
            if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
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
                firebaseService.setProfile(it) { (status, _, exception) ->
                    if (status == Constants.Status.Failure)
                        result.postValue(Resource.error(exception!!))
                }
            }
        }
        userModel.addSource(dataRepository.getGroups(id)) {
            if (it.isNotEmpty()) {
                it.forEach { (from, to) ->
                    val idUser = if (to != id) to else from
                    var name: String
                    var message: String
                    firebaseService.getUser(idUser) { (status, data) ->
                        if (status == Constants.Status.Success) {
                            val userData = data!!
                            name = userData.name
                            userModel.addSource(dataRepository.getMessage(idUser)) { messageModel ->
                                if (messageModel != null) {
                                    message = messageModel.message
                                    addChat(idUser, name, message)
                                }
                            }
                        }
                    }
                }
            }
        }

        userModel.addSource(dataRepository.getExpedient()) {
            it.forEach { expedient ->
                firebaseService.setExpedient(expedient) { (status, _, exception) ->
                    if (status == Constants.Status.Failure) {
                        result.postValue(Resource.error(exception!!))
                    }
                }
            }
        }

        userModel.addSource(dataRepository.getPatients()) {
            it.forEach { patient ->
                firebaseService.setPatient(patient) { (status, _, exception) ->
                    if (status == Constants.Status.Failure) {
                        result.postValue(Resource.error(exception!!))
                    }
                }
            }
        }

        userModel.addSource(dataRepository.getConsultation()) {
            if (it.isNotEmpty()) firebaseService.setConsultation(it) { (status, _, exception) ->
                if (status == Constants.Status.Failure)
                    result.postValue(Resource.error(exception!!))
            }
        }

    }

    private fun getExoskeleton() {
        firebaseService.getExoskeleton {
            if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.status == Constants.Status.Success) dataRepository.insertExoskeleton(it.data!!)
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

    private fun addChat(idUser: String, name: String, message: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (idUser != id) dataRepository.insertChat(ChatsEntity(idUser, name, message))
            }
        }
    }
}