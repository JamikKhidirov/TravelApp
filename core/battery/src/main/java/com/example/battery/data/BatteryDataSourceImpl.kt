package com.example.battery.data

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.example.battery.domain.BatteryDataSource
import com.example.battery.domain.model.BatteryInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class BatteryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val batteryManager: BatteryManager
) : BatteryDataSource {

    override fun getBatteryInfoFlow(): Flow<BatteryInfo> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                try {
                    val info = parseBatteryInfo(intent)
                    val result = trySend(info)
                    if (result.isFailure) {
                        Log.e("BatteryDataSource", "Не удалось отправить данные: ${result.exceptionOrNull()}")
                        close(result.exceptionOrNull())
                    }
                } catch (e: Exception) {
                    Log.e("BatteryDataSource", "Ошибка в onReceive", e)
                    close(e)
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {
                Log.e("BatteryDataSource", "Ошибка при отписке", e)
            }
        }
    }

    private fun parseBatteryInfo(intent: Intent): BatteryInfo {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else 0

        val statusRaw = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = statusRaw == BatteryManager.BATTERY_STATUS_CHARGING ||
                statusRaw == BatteryManager.BATTERY_STATUS_FULL

        val status = when (statusRaw) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Заряжается"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Разряжается"
            BatteryManager.BATTERY_STATUS_FULL -> "Заряжен полностью"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Не заряжается"
            else -> "Неизвестно"
        }

        val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val plugType = when (chargePlug) {
            BatteryManager.BATTERY_PLUGGED_AC -> "Сеть (AC)"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Беспроводная"
            else -> "От батареи"
        }

        val healthRaw = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val health = when (healthRaw) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Хорошее"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Перегрев"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Неисправна"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Перенапряжение"
            else -> "Неизвестно"
        }

        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val tempTenths = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val temperature = tempTenths / 10f
        val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion"

        val capacity = getBatteryCapacity(context)

        return BatteryInfo(
            level = batteryPct,
            isCharging = isCharging,
            status = status,
            plugType = plugType,
            health = health,
            voltage = voltage,
            temperature = temperature,
            technology = technology,
            capacitymAh = capacity
        )
    }

    @SuppressLint("PrivateApi")
    override fun getBatteryCapacity(context: Context): Double {
        return try {
            val powerProfileClass = Class.forName("com.android.internal.os.PowerProfile")
            val powerProfile = powerProfileClass
                .getConstructor(Context::class.java)
                .newInstance(context)
            powerProfileClass
                .getMethod("getBatteryCapacity")
                .invoke(powerProfile) as Double
        } catch (e: Exception) {
            Log.e("BatteryDataSource", "Ошибка получения емкости", e)
            0.0
        }
    }
}