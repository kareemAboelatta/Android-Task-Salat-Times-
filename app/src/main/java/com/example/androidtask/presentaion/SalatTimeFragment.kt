package com.example.androidtask.presentaion

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.androidtask.R
import com.example.androidtask.core.Resource
import com.example.androidtask.core.utils.GeocoderHelper
import com.example.androidtask.core.utils.LocationHelper
import com.example.androidtask.core.utils.Utils.convertDateTimeToString
import com.google.android.material.snackbar.Snackbar
import com.mikesu.horizontalexpcalendar.HorizontalExpCalendar
import com.mikesu.horizontalexpcalendar.common.Config
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_salat_times.*
import org.joda.time.DateTime
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class SalatTimeFragment : Fragment(R.layout.fragment_salat_times)  , EasyPermissions.PermissionCallbacks{
    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private val viewModel by viewModels<SalatTimeViewModel>()
    var currentCity = "Cairo" // initial value
    var currentDate = "0" // initial value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestLocationPermission()


        expCalender.setHorizontalExpCalListener(object :
            HorizontalExpCalendar.HorizontalExpCalListener {
            override fun onCalendarScroll(dateTime: DateTime) {
                currentDate= convertDateTimeToString(dateTime)
                viewModel.getSalatTimes(
                    date = currentDate,
                    address = currentCity
                )
            }

            override fun onDateSelected(dateTime: DateTime) {
                currentDate= convertDateTimeToString(dateTime)
                viewModel.getSalatTimes(
                    date = currentDate,
                    address = currentCity
                )
            }

            override fun onChangeViewPager(viewPagerType: Config.ViewPagerType?) {
                Log.e("Tag",  "")
            }

        })


        lifecycleScope.launchWhenStarted {
            viewModel.timingsState.collect{
                when(it){
                    is Resource.Success -> {
                        progressbar_spin_kit.visibility = View.GONE
                        tv_fajr.text=it.data?.Fajr
                        tv_sunrise.text=it.data?.Sunrise
                        tv_aduhr.text=it.data?.Dhuhr
                        tv_asr.text=it.data?.Asr
                        tv_sunset.text=it.data?.Sunset
                        tv_maghreb.text=it.data?.Maghrib
                        tv_esha.text=it.data?.Isha
                    }
                    is Resource.Error -> {
                        progressbar_spin_kit.visibility = View.GONE

                        Log.e("HomeFragment", "error" + it.message)
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {
                        Log.e("HomeFragment", "loading" )
                        progress_text.text="getting timings..."
                        progressbar_spin_kit.visibility = View.VISIBLE
                        spin_Kit.visibility = View.VISIBLE

                    }
                }

            }
        }


    }


    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without Location Permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermission()
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
        getLocation()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun getLocation() {
        if (hasLocationPermission()) {
            LocationHelper(
                activity = requireActivity(),
                onLocationChange = {
                    currentCity = GeocoderHelper.getCity(requireActivity(),it.latitude, it.longitude)
                    Toast.makeText(requireContext(), "$currentCity", Toast.LENGTH_SHORT).show()
                    address.text=currentCity
                    progressbar_spin_kit.visibility = View.GONE
                }
            ).startLocationUpdates()
            progressbar_spin_kit.visibility = View.VISIBLE


        } else {
            requestLocationPermission()
        }
    }



}