package com.example.travelapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.navigation.NavHostApp
import com.example.travelapp.ui.theme.TravelAppTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //Разрешение выдано можно вытаскивать геолоквцию пользователя
        } else {
            // Показать сообщение: "Нужно разрешение для работы с геолокацией"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAppTheme {
                val navHostController = rememberNavController()
                NavHostApp(
                    navHostController = navHostController
                )
            }
        }
    }


    //Функция проверяет есть ли разрешение на приблизительную или точную геолокацию
    private fun hasLocationPermission(): Boolean {
        return listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).any { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}

