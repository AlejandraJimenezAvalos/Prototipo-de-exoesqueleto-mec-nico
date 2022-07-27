package com.example.exoesqueletov1.ui.fragments.patient.ui.expedients

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpedientViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {
    val expedients = MediatorLiveData<List<ExpedientModel>>().apply {
        addSource(dataRepository.getExpedients(patientRepository.getId())) {
            if (it != null) value = it
        }
    }
}