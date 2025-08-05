package com.example.mvvmapp.viewmodel

import android.app.Application
import android.hardware.Sensor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mvvmapp.model.SensorDataModel
import com.example.mvvmapp.model.SensorRepository

class SensorViewModel(application: Application) : AndroidViewModel(application) {

    private val sensorRepository: SensorRepository = SensorRepository(application)

    val accelerometerData: LiveData<SensorDataModel.SensorData> = sensorRepository.accelerometerData
    val gyroscopeData: LiveData<SensorDataModel.SensorData> = sensorRepository.gyroscopeData

    val accelerometerMetadata: SensorDataModel.SensorMetadata? = sensorRepository.getMetadataForSensorType(Sensor.TYPE_ACCELEROMETER)
    val gyroscopeMetadata: SensorDataModel.SensorMetadata? = sensorRepository.getMetadataForSensorType(Sensor.TYPE_GYROSCOPE)

    override fun onCleared() {
        super.onCleared()
        sensorRepository.unregisterListeners()
    }
}
