package com.example.travelapp.ui.theme

import android.app.Activity

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OrangeMain,
    onPrimary = Color.Black,
    secondary = OrangeLight,
    background = DarkBack,
    surface = DarkSurface,
    onBackground = Color.White,
    surfaceVariant = Color(0xFF2C2C2C), // Базовый цвет скелетона в темной теме
    onSurfaceVariant = Color(0xFF3D3D3D), // Цвет "блика" шиммера,
    outline = Color.Gray // Для рамок поиска
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeMain,
    onPrimary = Color.White,
    secondary = OrangeDark,
    background = LightBack,
    surface = Color.White,
    onBackground = Color.Black,
    surfaceVariant = Color(0xFFE0E0E0), // Базовый цвет скелетона в светлой теме
    onSurfaceVariant = Color(0xFFF5F5F5), // Цвет "блика" шиммера, // Цвет неактивного текста (темно-серый)
    outline = Color.LightGray
)

@Composable
fun TravelAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}