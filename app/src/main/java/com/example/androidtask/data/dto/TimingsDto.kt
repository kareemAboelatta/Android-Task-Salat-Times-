package com.example.androidtask.data.dto

import com.example.androidtask.domain.models.ImportantTimings

data class TimingsDto(
    val code: Int,
    val `data`: Data,
    val status: String
)

