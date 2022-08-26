package com.example.androidtask.presentaion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtask.core.Resource
import com.example.androidtask.domain.models.ImportantTimings
import com.example.androidtask.domain.use_cases.GetSalatTimesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SalatTimeViewModel @Inject constructor(
    val getSalatTimesUseCase: GetSalatTimesUseCase
) : ViewModel(){

    private var _timingsState= MutableStateFlow<Resource<ImportantTimings, String>>(Resource.Empty())
    val timingsState: StateFlow<Resource<ImportantTimings , String>> = _timingsState


    fun getSalatTimes(date: String, address: String) {
        getSalatTimesUseCase(date, address).onEach {
            when (it) {
                is Resource.Loading -> _timingsState.value = Resource.Loading()
                is Resource.Success -> _timingsState.value = Resource.Success(it.data!!)
                is Resource.Error -> _timingsState.value = Resource.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }


}