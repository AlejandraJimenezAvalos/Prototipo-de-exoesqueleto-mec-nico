package com.example.exoesqueletov1.ui.fragments.chats

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.domain.DataRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dataRepository: DataRepository,
) : ViewModel() {

    val id = Firebase.auth.currentUser!!.uid

    val user = MediatorLiveData<List<ChatsEntity>>().apply {
        addSource(dataRepository.getChats()) {
            value = it
        }
    }

}