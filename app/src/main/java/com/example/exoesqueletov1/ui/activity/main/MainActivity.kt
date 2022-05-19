package com.example.exoesqueletov1.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.databinding.ActivityMainBinding
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.fragments.user.UserFragment
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.setError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogLoading = DialogLoading()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser {
            it.status.createLoadingDialog(
                supportFragmentManager,
                MainActivity::class.java.name,
                dialogLoading
            )
            when (it.status) {
                Constants.Status.Success -> {
                    binding.user = it.data!!
                }
                Constants.Status.Failure -> {
                    it.exception!!.setError(supportFragmentManager, MainActivity::class.java.name)
                }
                Constants.Status.NotExist -> {
                    setFragment(UserFragment())
                }
                else -> {}
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container_main, fragment).commit()
    }
}