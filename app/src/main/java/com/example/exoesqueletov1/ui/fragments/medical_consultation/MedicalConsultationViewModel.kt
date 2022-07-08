package com.example.exoesqueletov1.ui.fragments.medical_consultation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.domain.ConsultationTemporaryRepository
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicalConsultationViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val patientRepository: PatientRepository,
    private val consultationTemporaryRepository: ConsultationTemporaryRepository,
) : ViewModel() {

    fun getConsultation() = consultationTemporaryRepository.getConsultation()
    fun saveConsultation(consultationTemporary: ConsultationTemporary) =
        consultationTemporaryRepository.saveConsultation(consultationTemporary)

    val patient = MediatorLiveData<PatientModel>().apply {
        addSource(dataRepository.getPatient(patientRepository.getId())) {
            if (it != null) value = it
        }
    }
}