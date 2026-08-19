package com.example.battery.data

import android.os.BatteryManager
import com.example.battery.domain.BatteryDataSource
import com.example.battery.domain.model.BatteryInfo
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import javax.inject.Singleton



@ActivityRetainedScoped
class BatteryDataSourceImpl @Inject constructor(
    private val batteryManager: BatteryManager
): BatteryDataSource {


    override fun getBatteryInfo(): BatteryInfo {
        val level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val isCharging = batteryManager.isCharging
        return BatteryInfo(level = level, isCharging = isCharging)
    }

}