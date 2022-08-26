package com.example.androidtask.data.dto

import com.example.androidtask.domain.models.ImportantTimings

data class Timings(
    val Asr: String,
    val Dhuhr: String,
    val Fajr: String,
    val Firstthird: String,
    val Imsak: String,
    val Isha: String,
    val Lastthird: String,
    val Maghrib: String,
    val Midnight: String,
    val Sunrise: String,
    val Sunset: String
)

fun Timings.toImportantTimings(): ImportantTimings {
    return ImportantTimings(
        Fajr= Fajr,
        Sunrise = Sunrise,
        Dhuhr = Dhuhr,
        Asr = Asr,
        Sunset=Sunset,
        Maghrib = Maghrib,
        Isha = Isha
    )
}