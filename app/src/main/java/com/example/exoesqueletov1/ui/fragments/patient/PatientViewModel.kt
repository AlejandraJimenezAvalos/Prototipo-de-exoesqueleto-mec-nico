package com.example.exoesqueletov1.ui.fragments.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.UserRepository
import com.example.exoesqueletov1.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataRepository: DataRepository
) : ViewModel() {
    fun saveUser(
        name: String,
        yearsOld: String,
        gender: String,
        occupation: String,
        background: String,
        familyBackground: String,
        email: String,
        phone: String,
        address: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.insertPatient(
                    PatientModel(
                        UUID.randomUUID()!!.toString(),
                        name,
                        yearsOld,
                        gender,
                        email,
                        address,
                        phone,
                        userRepository.getId(),
                        occupation,
                        background,
                        familyBackground,
                        Constants.Origin.Create.toString(),
                    )
                )
            }
        }
    }

}