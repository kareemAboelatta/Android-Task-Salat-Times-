package com.example.androidtask.data.repository

import com.example.androidtask.data.dto.TimingsDto
import com.example.androidtask.data.remote.SalatTimesApi
import com.example.androidtask.domain.repository.Repository
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val api: SalatTimesApi
) : Repository {

    override suspend fun getSalatTimes(date: String, address: String): TimingsDto {
        return api.getSalatTimes(date, address)
    }
}