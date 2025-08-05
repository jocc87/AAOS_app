package com.example.mvvmapp.model

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.car.hardware.property.CarPropertyManager.CarPropertyEventCallback
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AutomotiveModel {
    private var mCar: Car? = null
    private var mCarPropertyManager: CarPropertyManager? = null

    private val _speed = MutableLiveData<Float>()
    val speed: LiveData<Float> get() = _speed

    private val _batteryLevel = MutableLiveData<Float>()
    val batteryLevel: LiveData<Float> get() = _batteryLevel

    private val _batteryUnit = MutableLiveData<Int>()
    val batteryUnit: LiveData<Int> get() = _batteryUnit

    private val _batteryCapacity = MutableLiveData<Float>()
    val batteryCapacity: LiveData<Float> get() = _batteryCapacity

    fun initializeCar(context: Context) {
        mCar = Car.createCar(context)
        mCarPropertyManager = mCar!!.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

        mCarPropertyManager!!.registerCallback(mCallBack,
            VehiclePropertyIds.PERF_VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        mCarPropertyManager?.registerCallback(mCallBack,
            VehiclePropertyIds.EV_BATTERY_LEVEL, CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        mCarPropertyManager?.registerCallback(mCallBack,
            VehiclePropertyIds.EV_BATTERY_DISPLAY_UNITS, CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        mCarPropertyManager?.registerCallback(mCallBack,
            VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY, CarPropertyManager.SENSOR_RATE_NORMAL
        )
    }

    private var mCallBack: CarPropertyEventCallback = object : CarPropertyEventCallback {
        override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
            if (carPropertyValue.propertyId == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                _speed.value = carPropertyValue.value as Float
            }
            if (carPropertyValue.propertyId == VehiclePropertyIds.EV_BATTERY_LEVEL) {
                _batteryLevel.value = carPropertyValue.value as Float
            }
            if (carPropertyValue.propertyId == VehiclePropertyIds.EV_BATTERY_DISPLAY_UNITS) {
                _batteryUnit.value = carPropertyValue.value as Int
            }
            if (carPropertyValue.propertyId == VehiclePropertyIds.INFO_EV_BATTERY_CAPACITY) {
                _batteryCapacity.value = carPropertyValue.value as Float
            }
        }

        override fun onErrorEvent(propertyId: Int, zone: Int) {
            Log.e("CarPropertyManager", "Error event: propertyId=$propertyId, zone=$zone")
        }
    }

    fun clear() {
        mCarPropertyManager?.unregisterCallback(mCallBack)
    }
}