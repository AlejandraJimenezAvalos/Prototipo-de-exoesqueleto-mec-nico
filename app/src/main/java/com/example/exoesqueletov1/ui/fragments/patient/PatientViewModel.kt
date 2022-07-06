package com.example.exoesqueletov1.ui.fragments.patient

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {
    val patient = MediatorLiveData<PatientModel>().apply {
        addSource(dataRepository.getPatient(patientRepository.getId())) {
            if (it != null) value = it
        }
    }
    val expedient = MediatorLiveData<List<ExpedientModel>>().apply {
        addSource(dataRepository.getExpedients(patientRepository.getId())) {
            if (it != null) value = it
        }
    }
}