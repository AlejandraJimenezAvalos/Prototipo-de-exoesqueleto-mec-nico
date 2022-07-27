package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.local.relations.ConsultationRelation
import com.example.exoesqueletov1.domain.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsultationViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    val consultation = MediatorLiveData<ConsultationRelation>().apply {
        addSource(dataRepository.getConsultationComplete()) {
            if (it != null) value = it
        }
    }
}