package com.example.exoesqueletov1.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val id = Firebase.auth.currentUser!!.uid

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