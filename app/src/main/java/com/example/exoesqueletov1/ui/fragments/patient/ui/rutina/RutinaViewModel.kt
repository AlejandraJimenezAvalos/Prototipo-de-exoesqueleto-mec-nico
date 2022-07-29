package com.example.exoesqueletov1.ui.fragments.patient.ui.rutina

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoesqueletov1.data.models.rutina.RutinaModel
import com.example.exoesqueletov1.domain.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RutinaViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    val listRutinas = MediatorLiveData<List<RutinaModel>>().apply {
        addSource(dataRepository.getRutinas()) {
            if (it.isNotEmpty()) value = it
        }
    }

    fun insertRutina(rutinaModel: RutinaModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dataRepository.insertRutina(rutinaModel)
            }
        }
    }

}