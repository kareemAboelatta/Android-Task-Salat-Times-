package com.example.androidtask.domain.use_cases

import android.location.Location
import com.example.androidtask.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke() : Flow<Location> = locationRepository.getLocation()

}