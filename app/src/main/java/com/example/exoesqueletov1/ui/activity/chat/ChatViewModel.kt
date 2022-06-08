package com.example.exoesqueletov1.ui.activity.chat

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.models.MessageModel
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val firebaseService: FirebaseService
) : ViewModel() {

    private val id = Firebase.auth.currentUser!!.uid
    val result = MediatorLiveData<Resource<ChatsEntity>>()

    fun getMessages(idUser: String) = MediatorLiveData<List<MessageModel>>().apply {
        addSource(dataRepository.getMessages(idUser)) {
            if (it.isNotEmpty()) {
                value = it!!
                addSource(dataRepository.readMessage(idUser)) { listMessages ->
                    if (listMessages.isNotEmpty()) listMessages.forEach { message ->
                        firebaseService.setMessage(message) { resultUpdate ->
                            if (resultUpdate.status == Constants.Status.Failure)
                                result.postValue(Resource.error(resultUpdate.exception!!))
                        }
                    }

                }
            }
        }
        result.addSource(dataRepository.getChat(idUser)) {
            if (it != null) result.postValue(Resource.success(it))
        }
    }

    fun setMessage(messageModel: MessageModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.insertMessage(messageModel)
            }
        }
        firebaseService.setMessage(messageModel) {
            if (it.status == Constants.Status.Failure) {
                result.postValue(Resource.error(it.exception!!))
            }
        }
    }

    init {
        firebaseService.getMessages(id) {
            if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it.status == Constants.Status.Success) dataRepository.insertMessage(it.data!!)
                }
            }
        }
    }

}