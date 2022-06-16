package com.example.exoesqueletov1.ui.activity

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.ActivitySplashBinding
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.utils.Constants.PERMISSIONS
import com.google.android.material.snackbar.Snackbar

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

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
                    if (checkPermissions(PERMISSIONS)) {
                        ActivityCompat.requestPermissions(
                            this@SplashActivity, PERMISSIONS,
                            1
                        )
                    } else {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                SingInActivity::class.java
                            )
                        )
                        finish()
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
                    var state = true
                    grantResults.forEach {
                        state = (it == PERMISSION_GRANTED) && state
                    }
                    if (!state) {
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