package com.example.battery.domain.model





data class BatteryInfo(
    val level: Int = 0,               // Уровень заряда в %
    val isCharging: Boolean = false,  // Заряжается ли
    val status: String = "Unknown",   // Разряжается / Заряжается / Полный
    val plugType: String = "None",    // AC (розетка), USB, Wireless
    val health: String = "Unknown",   // Состояние (Good, Overheat, Dead)
    val voltage: Int = 0,             // Напряжение в мВ
    val temperature: Float = 0f,      // Температура в °C
    val technology: String = "N/A",   // Технология (Li-ion, Li-poly)
    val capacitymAh: Double = 0.0     // Расчетная емкость в мА·ч
)
