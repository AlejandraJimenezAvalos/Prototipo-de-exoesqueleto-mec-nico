package com.example.exoesqueletov1.ui.fragments.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.domain.DataRepository
import com.example.exoesqueletov1.domain.UserRepository
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val usersLogged = MediatorLiveData<List<UsersEntity>>().apply {
        addSource(dataRepository.getUsers()) {
            if (it != null) value = it
        }
    }

    fun login(email: String, password: String): MutableLiveData<Resource<String>> {
        val result = MutableLiveData<Resource<String>>()
        result.postValue(Resource.loading())

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser!!
                    userRepository.setId(auth.currentUser!!.uid)
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            dataRepository.insertNewUser(
                                UsersEntity(
                                    id = auth.currentUser!!.uid,
                                    email = email,
                                    name = "",
                                    password = password,
                                    photoPath = ""
                                )
                            )
                        }
                    }
                }
                if (task.exception != null) result.postValue(Resource.error(task.exception!!))
            }
        return result
    }

    fun sendEmailReset(email: String): MutableLiveData<Resource<String>> {
        val result = MutableLiveData<Resource<String>>()
        result.postValue(Resource.loading())
        val url = "https://exoesqueletov1.page.link/__/auth/action?mode=action"
        val actionCodeSetting = ActionCodeSettings.newBuilder()
            .setUrl(url)
            .setIOSBundleId("com.example.exoesqueletov1")
            .setAndroidPackageName(
                "com.example.exoesqueletov1",
                false,
                null
            )
            .build()
        auth.sendPasswordResetEmail(email, actionCodeSetting).addOnSuccessListener {
            result.postValue(Resource.success(""))
        }.addOnFailureListener {
            result.postValue(Resource.error(it))
        }
        return result
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.deleteUsers(id)
            }
        }
    }
}