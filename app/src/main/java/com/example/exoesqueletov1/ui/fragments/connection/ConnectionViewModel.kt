package com.example.exoesqueletov1.ui.fragments.connection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    userRepository: UserRepository
) :
    ViewModel() {

    private val idUser = userRepository.getId()

    val user = MediatorLiveData<UserModel>().apply {
        addSource(dataRepository.getUser(idUser)) {
            if (it != null) value = it
        }
    }

    fun getPatient(idPatient: String) = MediatorLiveData<UserModel>().apply {
        addSource(dataRepository.getUser(idPatient)) {
            if (it != null) value = it
        }
    }


}