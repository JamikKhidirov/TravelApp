package com.example.battery.domain

import android.content.Context
import com.example.battery.domain.model.BatteryInfo
import kotlinx.coroutines.flow.Flow


interface BatteryDataSource {

    fun getBatteryInfoFlow(): Flow<BatteryInfo>



    fun getBatteryCapacity(context: Context): Double
}