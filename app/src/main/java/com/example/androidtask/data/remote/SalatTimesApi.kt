package com.example.androidtask.data.remote

import com.example.androidtask.data.dto.TimingsDto
import com.example.androidtask.domain.models.ImportantTimings
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SalatTimesApi {

    @GET("{date}?address=address")
    suspend fun getSalatTimes(
        @Path("date") date: String,
        @Query("address") address: String
    ): TimingsDto

}