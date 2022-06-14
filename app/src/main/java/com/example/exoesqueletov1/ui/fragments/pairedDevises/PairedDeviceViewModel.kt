package com.example.exoesqueletov1.ui.fragments.pairedDevises

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.domain.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PairedDeviceViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    val bluetoothDevice = MediatorLiveData<List<ExoskeletonQuery>>().apply {
        addSource(dataRepository.getExoskeleton()) {
            if (it.isNotEmpty()) value = it
        }
    }

}