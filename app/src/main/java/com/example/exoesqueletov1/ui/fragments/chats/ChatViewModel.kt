package com.example.exoesqueletov1.ui.fragments.chats

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.PatientRepository
import com.example.exoesqueletov1.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val patientRepository: PatientRepository,
    userRepository: UserRepository,
) : ViewModel() {

    fun savePatient(id: String, name: String) {
        patientRepository.setPatient(id, name)
    }

    val id = userRepository.getId()

    val user = MediatorLiveData<List<PatientModel>>().apply {
        addSource(dataRepository.getPatients()) {
            value = it
        }
    }

}