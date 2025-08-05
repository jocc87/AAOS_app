package com.example.mvvmapp.viewmodel

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmapp.view.MainActivity
import com.example.mvvmapp.model.AutomotiveModel

class AutomotiveViewModel : ViewModel() {
    private val model = AutomotiveModel()
    val speed: LiveData<Float> get() = model.speed
    val batteryLevel: LiveData<Float> get() = model.batteryLevel
    val batteryUnit: LiveData<Int> get() = model.batteryUnit
    val batteryCapacity: LiveData<Float> get() = model.batteryCapacity
    var permissionHandler =  PermissionHandler()

    fun initializeModel(context: Context) {
        model.initializeCar(context)
    }

    fun checkPermissions(activity: MainActivity){
        if (!permissionHandler.hasPermissions(activity)) {
            permissionHandler.requestPermission(activity)
        }
        else {
            initializeModel(activity)
        }
    }


    override fun onCleared() {
        super.onCleared()
        model.clear()
    }
}