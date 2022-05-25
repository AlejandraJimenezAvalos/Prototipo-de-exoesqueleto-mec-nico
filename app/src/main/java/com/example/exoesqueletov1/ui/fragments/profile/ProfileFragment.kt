package com.example.exoesqueletov1.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.databinding.FragmentProfileBinding
import com.example.exoesqueletov1.utils.Utils.getProfileBinding
import com.example.exoesqueletov1.utils.Utils.isEquals
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.profile.observe(viewLifecycleOwner) { binding.profile = it }
        viewModel.user.observe(viewLifecycleOwner) { binding.user = it }
        binding.buttonProfileViewSave.setOnClickListener {
            binding.getProfileBinding {
                it.isEquals(binding.profile) { state ->
                    if (!state) {
                        viewModel.setProfile(it)
                        Toast.makeText(context, "Datos guardados correctamente.", Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(context, "No hay cambios para guardar.", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }
}