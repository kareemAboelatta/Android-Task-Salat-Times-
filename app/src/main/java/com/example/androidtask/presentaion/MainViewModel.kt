package com.example.androidtask.presentaion

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtask.core.Resource
import com.example.androidtask.domain.models.ImportantTimings
import com.example.androidtask.domain.use_cases.GetLocationUseCase
import com.example.androidtask.domain.use_cases.GetSalatTimesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getSalatTimesUseCase: GetSalatTimesUseCase,
    val getLocationUseCase: GetLocationUseCase

) : ViewModel(){

    private var _timingsState= MutableStateFlow<Resource<ImportantTimings, String>>(Resource.Empty())
    val timingsState: StateFlow<Resource<ImportantTimings , String>> = _timingsState


    private var _locationState= MutableStateFlow<Resource<Location,String>>(Resource.Empty())
    val locationState: StateFlow<Resource<Location, String>> = _locationState

    fun getSalatTimes(date: String, address: String) {
        getSalatTimesUseCase(date, address).onEach {
            when (it) {
                is Resource.Loading -> _timingsState.value = Resource.Loading()
                is Resource.Success -> _timingsState.value = Resource.Success(it.data!!)
                is Resource.Error -> _timingsState.value = Resource.Error(it.message)
            }

        }.launchIn(viewModelScope)
    }

    fun getLocation(){
        viewModelScope.launch {
            _locationState.value=Resource.Loading()  // loading
            getLocationUseCase.invoke().catch {e->
                _locationState.value=Resource.Error(e.message) // error
            }.collect {
                Log.i("Address", "getLocation: $it")
                _locationState.value= Resource.Success(it)   // success
            }
        }
    }

}