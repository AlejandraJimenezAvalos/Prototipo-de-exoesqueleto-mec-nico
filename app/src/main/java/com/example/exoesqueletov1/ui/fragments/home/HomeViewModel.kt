package com.example.exoesqueletov1.ui.fragments.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {
    private val id = Firebase.auth.currentUser!!.uid
    val user = MediatorLiveData<UserModel>().apply {
        addSource(dataRepository.getUser(id)) {
            if (it != null) value = it
        }
    }
}