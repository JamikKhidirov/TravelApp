package com.example.battery.module


import android.content.Context
import android.os.BatteryManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.battery.data.BatteryDataSourceImpl
import com.example.battery.domain.BatteryDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test  // ✅ ВАЖНО: правильный импорт!
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BatteryModuleTest {

    private lateinit var batteryDataSource: BatteryDataSource
    private lateinit var batteryManager: BatteryManager
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        batteryDataSource = BatteryDataSourceImpl(context, batteryManager)
    }

    @Test
    fun dependencies_shouldBeCreated() {
        assertNotNull("Context should be created", context)
        assertNotNull("BatteryManager should be created", batteryManager)
        assertNotNull("BatteryDataSource should be created", batteryDataSource)
        assertTrue("BatteryDataSource should be BatteryDataSourceImpl",
            batteryDataSource is BatteryDataSourceImpl)
    }

    @Test  // ✅ Должна быть эта аннотация!
    fun batteryDataSource_shouldReturnBatteryInfo() = runTest {
        val flow = batteryDataSource.getBatteryInfoFlow()
        val batteryInfo = flow.first()

        assertNotNull("Battery info should not be null", batteryInfo)

        if (batteryInfo.level >= 0) {
            assertTrue("Battery level should be between 0 and 100",
                batteryInfo.level in 0..100)
        } else {
            println("⚠️ Уровень батареи недоступен на эмуляторе: ${batteryInfo.level}")
        }

        println("=== 🧪 РЕЗУЛЬТАТЫ ТЕСТА ===")
        println("🔋 Уровень: ${batteryInfo.level}%")
        println("⚡ Зарядка: ${batteryInfo.isCharging}")
        println("📊 Статус: ${batteryInfo.status}")
        println("🔌 Плагин: ${batteryInfo.plugType}")
        println("❤️ Здоровье: ${batteryInfo.health}")
        println("⚡ Напряжение: ${batteryInfo.voltage} mV")
        println("🌡️ Температура: ${batteryInfo.temperature} °C")
        println("🔋 Технология: ${batteryInfo.technology}")
        println("📊 Емкость: ${batteryInfo.capacitymAh} mAh")
        println("================================")
    }

    @Test  // ✅ Должна быть эта аннотация!
    fun batteryDataSource_shouldDetectChargingStatus() = runTest {
        val flow = batteryDataSource.getBatteryInfoFlow()
        val batteryInfo = flow.first()

        val validStatuses = listOf(
            "Заряжается", "Разряжается", "Заряжен полностью",
            "Не заряжается", "Неизвестно"
        )
        assertTrue("Status should be one of valid values",
            batteryInfo.status in validStatuses)

        if (batteryInfo.status == "Заряжается" || batteryInfo.status == "Заряжен полностью") {
            assertTrue("isCharging should be true when charging", batteryInfo.isCharging)
        } else if (batteryInfo.status == "Разряжается" || batteryInfo.status == "Не заряжается") {
            assertFalse("isCharging should be false when not charging", batteryInfo.isCharging)
        }
    }

    @Test  // ✅ Должна быть эта аннотация!
    fun batteryDataSource_shouldReturnCapacity() {
        val dataSource = batteryDataSource as BatteryDataSourceImpl
        val capacity = dataSource.getBatteryCapacity(context)

        assertTrue("Capacity should be >= 0", capacity >= 0.0)
        if (capacity > 0) {
            assertTrue("Capacity should be reasonable (1000-6000 mAh)",
                capacity in 1000.0..6000.0)
        } else {
            println("⚠️ Емкость батареи недоступна на этом устройстве")
        }
        println("📊 Емкость батареи: $capacity mAh")
    }

    @Test  // ✅ Должна быть эта аннотация!
    fun batteryInfo_shouldHaveAllFields() = runTest {
        val flow = batteryDataSource.getBatteryInfoFlow()
        val batteryInfo = flow.first()

        with(batteryInfo) {
            assertNotNull("level should not be null", level)
            assertNotNull("isCharging should not be null", isCharging)
            assertNotNull("status should not be null", status)
            assertNotNull("plugType should not be null", plugType)
            assertNotNull("health should not be null", health)
            assertNotNull("voltage should not be null", voltage)
            assertNotNull("temperature should not be null", temperature)
            assertNotNull("technology should not be null", technology)
            assertNotNull("capacitymAh should not be null", capacitymAh)

            assertTrue("status should not be empty", status.isNotEmpty())
            assertTrue("plugType should not be empty", plugType.isNotEmpty())
            assertTrue("health should not be empty", health.isNotEmpty())
            assertTrue("technology should not be empty", technology.isNotEmpty())
        }
    }
}