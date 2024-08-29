package com.seogaemo.android_location_foreground

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.seogaemo.android_location_foreground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted && coarseLocationGranted) {
            startLocationService()
        } else {
            showPermissionDeniedMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionCheck()
    }

    private fun permissionCheck() {
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION

        if (ContextCompat.checkSelfPermission(this, fineLocationPermission) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, coarseLocationPermission) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
        } else {
            startLocationService()
        }
    }

    private fun startLocationService() {
        binding.locationButton.setOnClickListener {
            val serviceIntent = Intent(this, BackgroundLocationUpdateService::class.java)
            startService(serviceIntent)
            Toast.makeText(this@MainActivity, "실행", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, "위치 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
        permissionCheck()
    }
}