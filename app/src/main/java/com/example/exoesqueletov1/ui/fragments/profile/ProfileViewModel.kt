package com.example.exoesqueletov1.ui.fragments.profile

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    userRepository: UserRepository
) :
    ViewModel() {
    private val id = userRepository.getId()

    val profile = MediatorLiveData<ProfileModel>().apply {
        addSource(dataRepository.getProfile(id)) {
            if (it != null) value = it
        }
    }

    val user = MediatorLiveData<UserModel>().apply {
        addSource(dataRepository.getUser(id)) {
            if (it != null) value = it
        }
    }

    fun setProfile(profileModel: ProfileModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.insertProfile(profileModel)
            }
        }
    }
}