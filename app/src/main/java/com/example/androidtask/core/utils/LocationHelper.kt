package com.example.androidtask.core.utils

import android.app.Activity
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*

class LocationHelper(val activity: Activity, private val  onLocationChange: (Location) -> Unit) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private val tag = "Location"

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    onLocationChange(it)
                }
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 5000
            smallestDisplacement = 500f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun startLocationUpdates() {
        buildLocationRequest()
        buildLocationCallback()
        try {
            Log.d(tag, "Location started.")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(tag, "Lost location permissions. Couldn't remove updates. $exception")
            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopLocationUpdates() {
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "Location Callback removed.")
                } else {
                    Log.d(tag, "Failed to remove Location Callback.")
                }
            }
        } catch (exception: SecurityException) {
            Log.e(tag, "Lost location permissions. Couldn't remove updates. $exception")
        }
    }



}
