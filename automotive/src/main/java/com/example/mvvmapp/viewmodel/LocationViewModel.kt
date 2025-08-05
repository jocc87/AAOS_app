package com.example.mvvmapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mvvmapp.model.LocationModel

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private var locationListener: LocationModel = LocationModel(application)

    val locationData: LiveData<LocationModel.LocationData> = locationListener.locationData

}