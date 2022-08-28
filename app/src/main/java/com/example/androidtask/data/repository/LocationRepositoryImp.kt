package com.example.androidtask.data.repository

import android.location.Location
import com.example.androidtask.data.data_source.LocationDataSource
import com.example.androidtask.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImp @Inject constructor(
    private val googleLocationDataSource: LocationDataSource
) : LocationRepository {

    override fun getLocation(): Flow<Location> {
        return googleLocationDataSource.fetchUpdates()
    }

}