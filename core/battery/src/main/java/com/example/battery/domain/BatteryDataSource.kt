package com.example.battery.domain

import com.example.battery.domain.model.BatteryInfo


interface BatteryDataSource {

    fun getBatteryInfo(): BatteryInfo
}