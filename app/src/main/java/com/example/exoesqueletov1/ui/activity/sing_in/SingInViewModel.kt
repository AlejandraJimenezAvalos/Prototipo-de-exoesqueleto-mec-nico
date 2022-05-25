package com.example.exoesqueletov1.ui.activity.sing_in

import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.domain.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingInViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

}