package com.example.androidtask.domain.repository

import com.example.androidtask.data.dto.TimingsDto

interface Repository {

    suspend fun getSalatTimes(date: String,address: String): TimingsDto
}