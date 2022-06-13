package com.example.exoesqueletov1.ui.activity.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.ActivityMainBinding
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.example.exoesqueletov1.ui.fragments.connection.ConnectionFragment
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loading = DialogLoading()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.userModel.observe(this) {
            val navView = when (it.user.getTypeUser()) {
                Constants.TypeUser.Admin -> {
                    binding.navViewPatient.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navView.visibility = View.VISIBLE
                    binding.navView
                }
                Constants.TypeUser.Specialist -> {
                    binding.navViewPatient.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.VISIBLE
                    binding.navView.visibility = View.GONE
                    binding.navViewSpecialist
                }
                Constants.TypeUser.Patient -> {
                    binding.navViewPatient.visibility = View.VISIBLE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navView.visibility = View.GONE
                    binding.navViewPatient
                }
            }
            val navController = findNavController(R.id.nav_host_fragment_activity_main_bottom)
            navView.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.chatActivity)
                    Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
            }
        }

        /*binding.motion.setTransitionDuration(1000)
        binding.motion.setTransition(R.id.start, R.id.open_terminal)
        binding.motion.transitionToEnd()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bluetooth_content, ConnectionFragment()).commit()
*/
        viewModel.result.observe(this) {
            it.status.createLoadingDialog(
                supportFragmentManager,
                SingInActivity::class.java.name,
                loading
            )
            if (it.status == Constants.Status.Failure) {
                val errorDialog = DialogOops(it.exception!!.message)
                errorDialog.show(supportFragmentManager, MainActivity::class.java.name)
            }
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sing_out -> {
                    viewModel.singOut()
                    startActivity(Intent(this, SingInActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && checkPermissions(Constants.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, Constants.PERMISSIONS, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == 1) && (grantResults.isNotEmpty())) {
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
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

    private fun checkPermissions(permissions: Array<String>): Boolean {
        var permissionResult = true
        permissions.forEach {
            val permissionState = ContextCompat.checkSelfPermission(this, it)
            permissionResult =
                permissionResult && (permissionState == PackageManager.PERMISSION_GRANTED)
        }
        return !permissionResult
    }
}