package com.example.battery.data

import android.content.Context
import android.os.BatteryManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BatteryDataSourceImplTest {

    private lateinit var batteryDataSource: BatteryDataSourceImpl
    private lateinit var context: Context
    private lateinit var batteryManager: BatteryManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        batteryDataSource = BatteryDataSourceImpl(context, batteryManager)
    }

    // ============================================================
    // ТЕСТ 1: Проверка создания экземпляра
    // ============================================================
    @Test
    fun batteryDataSource_shouldBeCreated() {
        assertNotNull("BatteryDataSource should be created", batteryDataSource)
        assertNotNull("Context should be created", context)
        assertNotNull("BatteryManager should be created", batteryManager)
    }

    // ============================================================
    // ТЕСТ 2: Проверка получения данных о батарее
    // ============================================================
    @Test
    fun getBatteryInfoFlow_shouldReturnBatteryInfo() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
        assertNotNull("Battery info should not be null", batteryInfo)

        // На эмуляторе может быть -1, поэтому проверяем корректность
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

    // ============================================================
    // ТЕСТ 3: Проверка статуса зарядки
    // ============================================================
    @Test
    fun getBatteryInfoFlow_shouldDetectChargingStatus() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
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

    // ============================================================
    // ТЕСТ 4: Проверка типа подключения
    // ============================================================
    @Test
    fun getBatteryInfoFlow_shouldDetectPlugType() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
        val validPlugTypes = listOf(
            "Сеть (AC)", "USB", "Беспроводная", "От батареи"
        )
        assertTrue("Plug type should be one of valid values",
            batteryInfo.plugType in validPlugTypes)
    }

    // ============================================================
    // ТЕСТ 5: Проверка состояния здоровья батареи
    // ============================================================
    @Test
    fun getBatteryInfoFlow_shouldDetectHealth() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
        val validHealthStatuses = listOf(
            "Хорошее", "Перегрев", "Неисправна", "Перенапряжение", "Неизвестно"
        )
        assertTrue("Health should be one of valid values",
            batteryInfo.health in validHealthStatuses)
    }

    // ============================================================
    // ТЕСТ 6: Проверка емкости батареи
    // ============================================================
    @Test
    fun getBatteryCapacity_shouldReturnCapacity() {
        // When
        val capacity = batteryDataSource.getBatteryCapacity(context)

        // Then
        assertTrue("Capacity should be >= 0", capacity >= 0.0)
        if (capacity > 0) {
            assertTrue("Capacity should be reasonable (1000-6000 mAh)",
                capacity in 1000.0..6000.0)
        } else {
            println("⚠️ Емкость батареи недоступна на этом устройстве")
        }
        println("📊 Емкость батареи: $capacity mAh")
    }

    // ============================================================
    // ТЕСТ 7: Проверка всех полей
    // ============================================================
    @Test
    fun batteryInfo_shouldHaveAllFields() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
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

    // ============================================================
    // ТЕСТ 8: Проверка, что Flow не пустой
    // ============================================================
    @Test
    fun getBatteryInfoFlow_shouldNotBeEmpty() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
        assertNotNull("Battery info should not be null", batteryInfo)
        assertTrue("Level should be valid", batteryInfo.level >= -1)
    }

    // ============================================================
    // ТЕСТ 9: Проверка isCharging не null
    // ============================================================
    @Test
    fun getBatteryInfoFlow_isChargingShouldNotBeNull() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
        assertNotNull("isCharging should not be null", batteryInfo.isCharging)
    }

    // ============================================================
    // ТЕСТ 10: Проверка напряжения
    // ============================================================
    @Test
    fun getBatteryInfoFlow_voltageShouldNotBeNegative() = runTest {
        // Given
        val flow = batteryDataSource.getBatteryInfoFlow()

        // When
        val batteryInfo = flow.first()

        // Then
        assertTrue("Voltage should not be negative", batteryInfo.voltage >= 0)
    }
}