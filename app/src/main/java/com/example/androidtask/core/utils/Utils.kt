package com.example.androidtask.core.utils

import org.joda.time.DateTime

object Utils {

     fun convertDateTimeToString(dateTime: DateTime):String=
        "${dateTime.year}-${dateTime.monthOfYear}-${dateTime.dayOfMonth}"

}