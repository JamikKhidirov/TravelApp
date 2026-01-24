package com.example.travelapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
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
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        if (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
            || permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true){
            //Разрешение на  геолокацию есть можем вытаскивать геолокацию
        }
        else {
            Toast.makeText(this, "Без локации мы не найдем туры рядом", Toast.LENGTH_LONG).show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        setContent {
            TravelAppTheme {
                val navHostController = rememberNavController()
                NavHostApp(
                    navHostController = navHostController
                )
            }
        }
    }

}

