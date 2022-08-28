package com.example.androidtask.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocation(): Flow<Location>
}