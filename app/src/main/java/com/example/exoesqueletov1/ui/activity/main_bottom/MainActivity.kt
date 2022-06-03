package com.example.exoesqueletov1.ui.activity.main_bottom

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.ActivityMainBottomBinding
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import dagger.hilt.android.AndroidEntryPoint
|
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBottomBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBottomBinding.inflate(layoutInflater)
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
                    binding.navView.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navViewSpecialist
                }
                Constants.TypeUser.Patient -> {
                    binding.navView.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navViewPatient
                }
            }
            val navController = findNavController(R.id.nav_host_fragment_activity_main_bottom)
            navView.setupWithNavController(navController)
        }

        binding.buttonLogOut.setOnClickListener {
            viewModel.singOut()
            startActivity(Intent(this, SingInActivity::class.java))
            finish()
        }
    }
}