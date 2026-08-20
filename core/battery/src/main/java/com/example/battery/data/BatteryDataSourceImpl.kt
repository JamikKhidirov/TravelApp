package com.example.battery.data

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
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
): BatteryDataSource {


    override fun getBatteryInfoFlow(): Flow<BatteryInfo> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = if (level != -1 && scale != -1) (level * 100 / scale.toFloat()).toInt() else 0

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

                // Расчет емкости батареи через системную профилировку
                val capacity = getBatteryCapacity(ctx)

                val info = BatteryInfo(
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

                trySend(info)
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)

        // При отмене подписки отключаем Receiver (защита от утечек)
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    @SuppressLint("PrivateApi")
    override fun getBatteryCapacity(context: Context): Double {
        val powerProfileClass = "com.android.internal.os.PowerProfile"
        return try {
            val powerProfile = Class.forName(powerProfileClass)
                .getConstructor(Context::class.java)
                .newInstance(context)
            Class.forName(powerProfileClass)
                .getMethod("getBatteryCapacity")
                .invoke(powerProfile) as Double
        } catch (e: Exception) {
            0.0
        }
    }

}

