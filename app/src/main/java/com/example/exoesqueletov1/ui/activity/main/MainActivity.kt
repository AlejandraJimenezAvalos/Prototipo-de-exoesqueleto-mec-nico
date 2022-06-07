package com.example.exoesqueletov1.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.ActivityMainBinding
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getTypeUser
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
                    binding.navView.visibility = View.VISIBLE
                    binding.navViewSpecialist
                }
                Constants.TypeUser.Patient -> {
                    binding.navViewPatient.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navView.visibility = View.VISIBLE
                    binding.navViewPatient
                }
            }
            val navController = findNavController(R.id.nav_host_fragment_activity_main_bottom)
            navView.setupWithNavController(navController)
        }

        /*binding.motion.setTransitionDuration(1000)
        binding.motion.setTransition(R.id.start, R.id.open_terminal)
        binding.motion.transitionToEnd()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bluetooth_content, LargeConnectionFragment()).commit()*/

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
    }
}