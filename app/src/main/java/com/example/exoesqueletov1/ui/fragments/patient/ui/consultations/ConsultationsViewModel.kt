package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.consultation.ConsultationData
import com.example.exoesqueletov1.domain.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsultationsViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val consultations = MediatorLiveData<List<ConsultationData>>().apply {
        addSource(dataRepository.getConsultationsByIdPatient()) {
            if (it.isNotEmpty()) value = it
        }
    }
}