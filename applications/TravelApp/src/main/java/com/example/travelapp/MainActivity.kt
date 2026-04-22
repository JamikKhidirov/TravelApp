package com.example.travelapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.location.data.LocationClientImpl
import com.example.location.domain.LocationClient
import com.example.navigation.NavHostApp
import com.example.travelapp.ui.theme.TravelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var location: LocationClient

    //Текущая геолокация
    var currentLocation: Location? = null


    private var keepSplashScreen: Boolean = true

    private val locationPermissionLauncher = registerForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        if (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
            || permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true){
            //Разрешение на геолокацию есть можем вытаскивать геолокацию


            // В колбэке разрешений:
            manageLocationService(LocationService.ACTION_START)
            startLocationUpdates()


        }
        else {
            Toast.makeText(this, "Без локации мы не найдем туры рядом", Toast.LENGTH_LONG).show()

        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()

        //Устанавливаем условие удержания сплеша
        splashScreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        // 3. Таймер на 2 секунды
        lifecycleScope.launch {
            delay(800)
            keepSplashScreen = false
        }

        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        window.navigationBarColor = Color.TRANSPARENT

        setContent {
            TravelAppTheme {
                val navHostController = rememberNavController()
                NavHostApp(
                    navHostController = navHostController
                )
            }
        }

        //ПРИНУДИТЕЛЬНО делаем nav bar прозрачным
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightNavigationBars = false
        window.navigationBarColor = Color.TRANSPARENT
        window.isNavigationBarContrastEnforced = false
    }

    private fun startLocationUpdates() {
        lifecycleScope.launch {
            // Используем твой метод Flow
            location.getLocationUpdates(5000L) // например, каждые 5 сек
                .catch { e ->
                    // Обработка ошибок (например, если GPS выключен)
                    Log.e("Location", "Ошибка получения локации: ${e.message}")
                }
                .collect { location ->
                    if (location != null) {
                        currentLocation = location
                        Log.d("Location", "Новая локация: ${location.latitude}, ${location.longitude}")
                        // Здесь можно обновить состояние или отправить данные в ViewModel
                    }
                }
        }
    }


    private fun manageLocationService(action: String) {
        val intent = Intent(this, LocationService::class.java).apply {
            this.action = action
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }




}

