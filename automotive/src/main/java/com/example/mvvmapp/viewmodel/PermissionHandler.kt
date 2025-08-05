package com.example.mvvmapp.viewmodel

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.mvvmapp.view.MainActivity

class PermissionHandler {
    val perms = arrayOf(
        "android.car.permission.CAR_INFO",
        "android.car.permission.CAR_SPEED",
        "android.car.permission.CAR_ENERGY",
        "android.permission.ACCESS_FINE_LOCATION",
    )

    val maxPermissionRequests = 2
    val permsRequestCode: Int = 200
    val permissionStatus: MutableMap<String, Boolean> = mutableMapOf()
    val _deniedPermissions = mutableListOf<String>()


    init {
        // Initialize all permissions to false
        for (permission in perms) {
            permissionStatus[permission] = false
        }
    }

    fun requestPermission(activity: MainActivity) {
        ActivityCompat.requestPermissions(activity, perms, permsRequestCode)
        updatePermissionsStatus(activity)
    }

    fun hasPermissions(context: Context): Boolean {
        for (permission in perms) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // Update permission statuses after requestPermissions result
    private fun updatePermissionsStatus(context: Context) {
        for (permission in perms) {
            permissionStatus[permission] = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Get list of granted permissions
    fun getGrantedPermissions(): List<String> {
        return permissionStatus.filter { it.value }.keys.toList()
    }

    // Get list of denied permissions
    fun getDeniedPermissions(): List<String> {
        return permissionStatus.filter { !it.value }.keys.toList()
    }

    // Check if a specific permission is granted
    fun isPermissionGranted(permission: String): Boolean {
        return permissionStatus[permission] == true
    }
}