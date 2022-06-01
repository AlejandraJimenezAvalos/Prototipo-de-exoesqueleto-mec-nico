package com.example.exoesqueletov1.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.ActivitySplashBinding
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.google.android.material.snackbar.Snackbar

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    val coarsePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            startActivity(
                Intent(
                    this@SplashActivity,
                    SingInActivity::class.java
                )
            )
            finish()
        } else {
            Snackbar.make(
                binding.root,
                "No se puede continuar por no contar con los permisos requeridos.",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Aceptar") {
                finish()
            }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arranque()
    }

    override fun onResume() {
        super.onResume()
        val timer: Thread = object : Thread() {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val permissions = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                    if (checkPermissions(permissions)) {
                        ActivityCompat.requestPermissions(
                            this@SplashActivity, permissions,
                            1
                        )
                    }
                }
            }
        }
        timer.start()
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        var permissionResult = true
        permissions.forEach {
            val permissionState = ContextCompat.checkSelfPermission(this, it)
            permissionResult = permissionResult && (permissionState == PERMISSION_GRANTED)
        }
        return !permissionResult
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()) {
                    grantResults.forEach {
                        if (it == PERMISSION_GRANTED) {
                            startActivity(
                                Intent(
                                    this@SplashActivity,
                                    SingInActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            Snackbar.make(
                                binding.root,
                                "No se puede continuar por no contar con los permisos requeridos.",
                                Snackbar.LENGTH_INDEFINITE
                            ).setAction("Aceptar") {
                                finish()
                            }.show()
                        }
                    }
                }
            }
            else -> {}
        }
    }

    private fun arranque() {
        val titulo = findViewById<ImageView>(R.id.titulo_image)
        val logo = findViewById<ImageView>(R.id.logo_image)
        val animation = AnimationUtils.loadAnimation(this, R.anim.transition)
        titulo.startAnimation(animation)
        logo.startAnimation(animation)
    }
}