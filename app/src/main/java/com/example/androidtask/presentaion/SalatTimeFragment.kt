package com.example.androidtask.presentaion

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.androidtask.R
import com.example.androidtask.core.Resource
import com.example.androidtask.core.utils.GeocoderHelper
import com.example.androidtask.core.utils.Utils.convertDateTimeToString
import com.example.androidtask.databinding.FragmentSalatTimesBinding
import com.google.android.material.snackbar.Snackbar
import com.mikesu.horizontalexpcalendar.HorizontalExpCalendar
import com.mikesu.horizontalexpcalendar.common.Config
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class SalatTimeFragment : Fragment(R.layout.fragment_salat_times), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentSalatTimesBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private val viewModel by viewModels<MainViewModel>()

    var currentCity: String? = null // initial value
    var currentDate = "0" // initial value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocation()

        binding.expCalender.setHorizontalExpCalListener(object :
            HorizontalExpCalendar.HorizontalExpCalListener {
            override fun onCalendarScroll(dateTime: DateTime) {
                currentDate = convertDateTimeToString(dateTime)
                if (currentCity != null) {
                    viewModel.getSalatTimes(
                        date = currentDate,
                        address = currentCity!!
                    )
                }
            }
            override fun onDateSelected(dateTime: DateTime) {
                currentDate = convertDateTimeToString(dateTime)
                if (currentCity != null) {
                    viewModel.getSalatTimes(
                        date = currentDate,
                        address = currentCity!!
                    )
                }
            }
            //this don't
            override fun onChangeViewPager(viewPagerType: Config.ViewPagerType?) {
                Log.i("Tag", "")
            }

        })

        lifecycleScope.launchWhenStarted {
            viewModel.timingsState.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.progressbarSpinKit.visibility = View.GONE
                        binding.tvFajr.text = it.data?.Fajr
                        binding.tvSunrise.text = it.data?.Sunrise
                        binding.tvAduhr.text = it.data?.Dhuhr
                        binding.tvAsr.text = it.data?.Asr
                        binding.tvSunset.text = it.data?.Sunset
                        binding.tvMaghreb.text = it.data?.Maghrib
                        binding.tvEsha.text = it.data?.Isha
                    }
                    is Resource.Error -> {
                        binding.progressbarSpinKit.visibility = View.GONE

                        Log.i("HomeFragment", "error" + it.message)
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {
                        Log.i("HomeFragment", "loading")
                        binding.progressText.text = "getting timings..."
                        binding.progressbarSpinKit.visibility = View.VISIBLE
                        binding.spinKit.visibility = View.VISIBLE

                    }
                }

            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.locationState.collect {
                when (it) {
                    is Resource.Success -> {
                        var location = it.data!!

                        val city = GeocoderHelper.getCity(
                            requireContext(),
                            location.latitude,
                            location.longitude
                        )
                        currentCity = city
                        binding.address.text = currentCity
                        viewModel.getSalatTimes(
                            date = currentDate,
                            address = currentCity!!
                        )
                    }
                    is Resource.Error -> {

                        Log.i("Address", "error" + it.message)
                        Snackbar.make(
                            requireView(), "error" + it.message, Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {
                        Log.i("Address", "loading")
                    }
                }

            }
        }

    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
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
            viewModel.getLocation()
        } else {
            requestLocationPermission()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalatTimesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}