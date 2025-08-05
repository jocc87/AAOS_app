package com.example.mvvmapp.model

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SensorRepository(application: Application) : SensorEventListener {

    private val sensorManager: SensorManager = application.getSystemService(SensorManager::class.java)

    private val _accelerometerData = MutableLiveData<SensorDataModel.SensorData>()
    val accelerometerData: LiveData<SensorDataModel.SensorData> get() = _accelerometerData

    private val _gyroscopeData = MutableLiveData<SensorDataModel.SensorData>()
    val gyroscopeData: LiveData<SensorDataModel.SensorData> get() = _gyroscopeData

    val sensorMetadataMap: Map<Int, SensorDataModel.SensorMetadata>

    private val accelerometer: Sensor?
    private val gyroscope: Sensor?

    init {
        val allSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        sensorMetadataMap = allSensors.associateBy(
            { it.type },
            {
                SensorDataModel.SensorMetadata(
                    name = it.name,
                    type = it.type,
                    vendor = it.vendor,
                    version = it.version,
                    maximumRange = it.maximumRange,
                    resolution = it.resolution,
                    power = it.power
                )
            }
        )

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        gyroscope?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    _accelerometerData.postValue(SensorDataModel.SensorData(x, y, z))
                }
                Sensor.TYPE_GYROSCOPE -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    _gyroscopeData.postValue(SensorDataModel.SensorData(x, y, z))
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            when (accuracy) {
                SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                    Log.d("Sensor", "Accelerometer accuracy is high.")
                }
                SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                    Log.d("Sensor", "Accelerometer accuracy is medium.")
                }
                SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                    Log.d("Sensor", "Accelerometer accuracy is low.")
                }
                SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                    Log.d("Sensor", "Accelerometer accuracy is unreliable.")
                }
            }
        }
    }

    fun unregisterListeners() {
        sensorManager.unregisterListener(this)
    }

    fun getMetadataForSensorType(sensorType: Int): SensorDataModel.SensorMetadata? {
        return sensorMetadataMap[sensorType]
    }
}