package com.example.codingchallenge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.codingchallenge.model.ItemViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Remove the app title bar
        supportActionBar?.hide()

        //Request permisions
        if (!arePermissionsGranted()) {
            grantPermissionsActivityResult.launch(REQUIRED_PERMISSIONS)
        } else {
            permissionsGranted = true
        }
    }

    private fun arePermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            applicationContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val grantPermissionsActivityResult =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var areAllPermissionsGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    areAllPermissionsGranted = false
            }
            if (!areAllPermissionsGranted) {
                permissionsGranted = false
                Toast.makeText(
                    baseContext,
                    R.string.app_will_not_work_permissions,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                permissionsGranted = true
            }
        }

    companion object {
        var permissionsGranted = false
        val REQUIRED_PERMISSIONS = mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

}