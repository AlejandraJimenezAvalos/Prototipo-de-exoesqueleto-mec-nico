package com.example.exoesqueletov1.ui.activity.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.utils.Constants
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
                if (it.data!!.id == id) getExoskeleton(it.data.user)
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        dataRepository.insertUser(it.data)
                    }
                }
                if (it.data.id == id) {
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
        firebaseService.getMessages(id) {
            if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.status == Constants.Status.Success) dataRepository.insertMessage(it.data!!)
                }
            }
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
        userModel.addSource(dataRepository.getGroups(id)) {
            if (it.isNotEmpty()) {
                it.forEach { groupsQuery ->
                    val idUser = if (groupsQuery.to != id) groupsQuery.to else groupsQuery.from
                    var name: String
                    var message: String
                    firebaseService.getUser(idUser) { resource ->
                        if (resource.status == Constants.Status.Success) {
                            val userData = resource.data!!
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

    }

    private fun getExoskeleton(user: String) {
        firebaseService.getExoskeleton(id, user) {
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