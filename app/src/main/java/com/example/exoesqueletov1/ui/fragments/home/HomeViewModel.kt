package com.example.exoesqueletov1.ui.fragments.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    userRepository: UserRepository
) : ViewModel() {
    private val id = userRepository.getId()
    val user = MediatorLiveData<UserModel>().apply {
        addSource(dataRepository.getUser(id)) {
            if (it != null) value = it
        }
    }
}