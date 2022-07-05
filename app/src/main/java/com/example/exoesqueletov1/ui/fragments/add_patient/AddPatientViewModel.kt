package com.example.exoesqueletov1.ui.fragments.add_patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.data.sharedpreferences.PatientTemporary
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.PatientTemporaryRepository
import com.example.exoesqueletov1.domain.UserRepository
import com.example.exoesqueletov1.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPatientViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataRepository: DataRepository,
    private val patientTemporaryRepository: PatientTemporaryRepository
) : ViewModel() {

    fun setPatientTemporary(patientTemporary: PatientTemporary) =
        patientTemporaryRepository.setPatient(patientTemporary)

    fun getPatientTemporary() = patientTemporaryRepository.getPatient()

    fun deletePatient() = patientTemporaryRepository.delete()

    fun saveUser(
        patientTemporary: PatientTemporary
    ) {
        patientTemporaryRepository.delete()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                patientTemporary.apply {
                    dataRepository.insertPatient(
                        PatientModel(
                            idPatient,
                            name,
                            birthday,
                            gender,
                            email,
                            address,
                            phone,
                            lada,
                            userRepository.getId(),
                            occupation,
                            Constants.Origin.Create.toString()
                        )
                    )
                    val list = mutableListOf<ExpedientModel>()
                    list.addAll(listAntecedents)
                    list.addAll(listHabits)
                    list.addAll(listCicatriz)
                    dataRepository.setExpedients(list)
                }
            }
        }
    }

}