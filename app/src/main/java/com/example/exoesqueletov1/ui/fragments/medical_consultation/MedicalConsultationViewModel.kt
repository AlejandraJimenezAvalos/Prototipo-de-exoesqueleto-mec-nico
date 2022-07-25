package com.example.exoesqueletov1.ui.fragments.medical_consultation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.local.sharedpreferences.ConsultationTemporary
import com.example.exoesqueletov1.data.models.Consultation
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.domain.ConsultationTemporaryRepository
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.PatientRepository
import com.example.exoesqueletov1.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MedicalConsultationViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val patientRepository: PatientRepository,
    private val userRepository: UserRepository,
    private val consultationTemporaryRepository: ConsultationTemporaryRepository,
) : ViewModel() {

    fun getConsultation() = consultationTemporaryRepository.getConsultation()
    fun saveConsultation(consultationTemporary: ConsultationTemporary) {
        consultationTemporary.idUser = userRepository.getId()
        consultationTemporary.idPatient = patientRepository.getId()
        consultationTemporaryRepository.saveConsultation(consultationTemporary)
    }

    fun saveConsultation(consultation: Consultation) {
        consultation.consultationData.idPatient = patientRepository.getId()
        consultation.consultationData.idUser = userRepository.getId()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.setConsultation(consultation)
            }
        }
    }

    val patient = MediatorLiveData<PatientModel>().apply {
        addSource(dataRepository.getPatient(patientRepository.getId())) {
            if (it != null) value = it
        }
    }
}