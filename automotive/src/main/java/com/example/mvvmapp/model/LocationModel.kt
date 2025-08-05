package com.example.mvvmapp.model
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmapp.view.MainActivity
import com.example.mvvmapp.viewmodel.PermissionHandler

class LocationModel(application: Application) : LocationListener {
    private var locationManager: LocationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val application: Application = application
    var permissionHandler =  PermissionHandler()

    private val _locationData = MutableLiveData<LocationData>()
    val locationData: LiveData<LocationData> get() = _locationData

    init {
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Engedély kérése
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
    }

    override fun onLocationChanged(location: Location) {
        println("New location: Latitude ${location.latitude}, Longitude ${location.longitude}")
        val lat = location.latitude
        val long = location.longitude
        val speed = location.speed
        val alt = location.altitude
        _locationData.postValue(LocationData(lat, long, speed, alt))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        println("Provider $provider status changed to $status")
    }

    override fun onProviderEnabled(provider: String) {
        println("Provider $provider is enabled")
    }

    override fun onProviderDisabled(provider: String) {
        println("Provider $provider is disabled")
    }

    data class LocationData(
        val latitude: Double,
        val longitude: Double,
        val speed: Float,
        val altitude: Double,
    )
}