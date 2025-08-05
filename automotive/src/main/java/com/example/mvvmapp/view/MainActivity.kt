package com.example.mvvmapp.view

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.mvvmapp.R
import com.example.mvvmapp.databinding.ActivityMainBinding
import com.example.mvvmapp.viewmodel.AutomotiveViewModel
import com.example.mvvmapp.viewmodel.LocationViewModel
import com.example.mvvmapp.viewmodel.SensorViewModel

class MainActivity : AppCompatActivity() {

    private val autoViewModel: AutomotiveViewModel by viewModels()
    private val sensorViewModel: SensorViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // Binding ViewModels to XML layout
        binding.sensorViewModel = sensorViewModel
        observeSensorData()
        binding.locationViewModel = locationViewModel
        observeLocationData()

        // Observe speed data from autoViewModel
        autoViewModel.speed.observe(this) { speed ->
            speed?.let {
                val kph: Float = it * 3.6F
                binding.awesomeSpeedometer.speedTo(kph, 300)
            }
        }

        autoViewModel.batteryLevel.observe(this) { batteryLevel ->
            // Convert battery level to an integer for the ProgressBar
            binding.batteryProgressBar.progress = batteryLevel.toInt()
        }

        autoViewModel.batteryCapacity.observe(this) { batteryCapacity ->
            // Convert battery level to an integer for the ProgressBar
            binding.batteryProgressBar.max = batteryCapacity.toInt()
        }

        autoViewModel.checkPermissions(this)
    }

    private fun observeSensorData() {
        sensorViewModel.gyroscopeData.observe(this, Observer { sensorData ->
            binding.textViewXg.text = "X: ${sensorData.x}"
            binding.textViewYg.text = "Y: ${sensorData.y}"
            binding.textViewZg.text = "Z: ${sensorData.z}"
        })

        sensorViewModel.accelerometerData.observe(this, Observer { sensorData ->
            binding.textViewX?.text = "X: ${sensorData.x}"
            binding.textViewY?.text = "Y: ${sensorData.y}"
            binding.textViewZ?.text = "Z: ${sensorData.z}"
        })
    }

    private fun observeLocationData() {
        locationViewModel.locationData.observe(this, Observer { locationData ->
            binding.locationLat?.text = "Latitude ${locationData.latitude}"
            binding.locationLong?.text = "Longitude: ${locationData.longitude}"
        })

    }

    var permissionRequestCount = 0

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == autoViewModel.permissionHandler.permsRequestCode) {
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    autoViewModel.permissionHandler._deniedPermissions.add(permissions[i])
                }
            }
            if (autoViewModel.permissionHandler._deniedPermissions.isEmpty()) {
                autoViewModel.initializeModel(this)
            } else {
                if (permissionRequestCount < autoViewModel.permissionHandler.maxPermissionRequests) {
                    permissionRequestCount++
                    requestPermissions(
                        autoViewModel.permissionHandler._deniedPermissions.toTypedArray(),
                        autoViewModel.permissionHandler.permsRequestCode
                    )
                } else {
                    Log.d(
                        "MainActivity",
                        "list of deny perms: ${autoViewModel.permissionHandler._deniedPermissions.joinToString()}"
                    )
                    autoViewModel.initializeModel(this)
                }
            }
        }
    }
}
