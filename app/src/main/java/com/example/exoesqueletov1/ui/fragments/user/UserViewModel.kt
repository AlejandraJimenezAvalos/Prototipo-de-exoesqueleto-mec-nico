package com.example.exoesqueletov1.ui.fragments.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.firebase.FirebaseService
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val firebaseService: FirebaseService) :
    ViewModel() {

        fun saveUser(user: UserModel): MutableLiveData<Resource<Void>> {
            val result = MutableLiveData<Resource<Void>>()
            firebaseService.setUser(user) {
                if (it.status == Constants.Status.Failure) result.postValue(Resource.error(it.exception!!))
                if (it.status == Constants.Status.Loading) result.postValue(Resource.loading())
                if (it.status == Constants.Status.Success) result.postValue(Resource.success(it.data))
            }
            return result
        }

}