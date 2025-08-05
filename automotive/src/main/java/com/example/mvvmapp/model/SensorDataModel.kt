package com.example.mvvmapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SensorDataModel {
    data class SensorData(
        val x: Float,
        val y: Float,
        val z: Float,
    )

    data class SensorMetadata(
        val name: String,
        val type: Int,
        val vendor: String,
        val version: Int,
        val maximumRange: Float,
        val resolution: Float,
        val power: Float
    )
}