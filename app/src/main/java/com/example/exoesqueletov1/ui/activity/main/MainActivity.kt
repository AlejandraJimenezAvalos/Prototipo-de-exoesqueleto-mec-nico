package com.example.exoesqueletov1.ui.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.databinding.ActivityMainBinding
import com.example.exoesqueletov1.databinding.ContentMainBinding
import com.example.exoesqueletov1.ui.activity.main.adapter.MenuAdapter
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.fragments.AssignWorkFragment
import com.example.exoesqueletov1.ui.fragments.NotificationFragment
import com.example.exoesqueletov1.ui.fragments.PairedDevisesFragment
import com.example.exoesqueletov1.ui.fragments.chats.ChatsFragment
import com.example.exoesqueletov1.ui.fragments.profile.ProfileFragment
import com.example.exoesqueletov1.ui.fragments.user.UserFragment
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Constants.Menu
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import com.example.exoesqueletov1.utils.Utils.setError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var bindingContent: ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogLoading = DialogLoading()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingContent = binding.contentMain
        setContentView(binding.root)

        viewModel.result.observe(this) {
            it.status.createLoadingDialog(
                supportFragmentManager,
                MainActivity::class.java.name,
                dialogLoading
            )
            when (it.status) {
                Constants.Status.Failure -> it.exception!!.setError(
                    supportFragmentManager,
                    MainActivity::class.java.name
                )
                Constants.Status.NotExist -> setFragment(UserFragment())
                else -> {}
            }
        }

        viewModel.userModel.observe(this) {
            binding.user = it!!
            setMenu(it.user.getTypeUser())
        }
        setFragment(NotificationFragment())
        binding.buttonFinishMain.setOnClickListener {
            finish()
        }
        binding.buttonSingoutMain.setOnClickListener {
            viewModel.singOut()
            startActivity(Intent(this, SingInActivity::class.java))
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMenu(typeUser: Constants.TypeUser) {
        val list = mutableListOf<Menu>()
        val adapter = MenuAdapter(list) {
            when (it) {
                Menu.Notification -> setFragment(NotificationFragment())
                Menu.Profile -> setFragment(ProfileFragment())
                Menu.Chats -> setFragment(ChatsFragment())
                Menu.Control -> setFragment(PairedDevisesFragment())
                Menu.WorkSpecialist -> setFragment(AssignWorkFragment())
            }
        }
        list.add(Menu.Notification)
        list.add(Menu.Profile)
        list.add(Menu.Chats)
        when (typeUser) {
            Constants.TypeUser.Admin -> {
                list.add(Menu.Control)
                list.add(Menu.WorkSpecialist)
            }
            Constants.TypeUser.Specialist -> list.add(Menu.WorkSpecialist)
            Constants.TypeUser.Patient -> list.add(Menu.Control)
        }
        bindingContent.recyclerMenu.adapter = adapter
        bindingContent.recyclerMenu.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter.notifyDataSetChanged()
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container_main, fragment).commit()
    }
}