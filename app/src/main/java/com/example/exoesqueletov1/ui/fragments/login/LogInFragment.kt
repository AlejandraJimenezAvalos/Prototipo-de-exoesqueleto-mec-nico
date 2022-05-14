package com.example.exoesqueletov1.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.utils.Utils.getText
import com.example.exoesqueletov1.data.utils.Utils.isNotEmpty
import com.example.exoesqueletov1.data.utils.Utils.isValidEmail
import com.example.exoesqueletov1.databinding.FragmentLogInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonLogin.setOnClickListener {
                val email = layoutEmailLogin.getText()
                val password = layoutPasswordLogin.getText()
                val emailState = layoutEmailLogin.isNotEmpty("Introduzca su correo electronico")
                val passwordState = layoutPasswordLogin.isNotEmpty("Introduzca su contraseña")
                if (emailState && passwordState)
                    if (layoutEmailLogin.isValidEmail()) viewModel.login(email, password)
            }
        }
    }

}