package com.example.exoesqueletov1.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getText
import com.example.exoesqueletov1.utils.Utils.isNotEmpty
import com.example.exoesqueletov1.utils.Utils.isValidEmail
import com.example.exoesqueletov1.utils.Utils.setError
import com.example.exoesqueletov1.databinding.FragmentLoginBinding
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var dialogOops: DialogOops

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val dialogLoading = DialogLoading()
            buttonLogin.setOnClickListener {
                val email = layoutEmailLogin.getText()
                val password = layoutPasswordLogin.getText()
                val emailState = layoutEmailLogin.isNotEmpty("Introduzca su correo electronico")
                val passwordState = layoutPasswordLogin.isNotEmpty("Introduzca su contraseña")
                if (emailState && passwordState)
                    if (layoutEmailLogin.isValidEmail())
                        viewModel.login(email, password).observe(viewLifecycleOwner) { resource ->
                            if (resource.status == Constants.Status.Failure) {
                                dialogOops = resource.exception!!.setError(
                                    childFragmentManager,
                                    LogInFragment::class.java.name
                                )
                            }
                            resource.status.createLoadingDialog(
                                childFragmentManager,
                                LogInFragment::class.java.name,
                                dialogLoading
                            )
                        }
            }
            buttonLostYourPassword.setOnClickListener {
                if (layoutEmailLogin.isNotEmpty("Introduce tu correo para enviar el correo de restablecimiento.")) {
                    if (layoutEmailLogin.isValidEmail()) {
                        viewModel.sendEmailReset(layoutEmailLogin.getText())
                            .observe(viewLifecycleOwner) {
                                it.status.createLoadingDialog(
                                    childFragmentManager,
                                    LogInFragment::class.java.name,
                                    dialogLoading
                                )
                                when (it.status) {
                                    Constants.Status.Success ->
                                        Snackbar.make(
                                            view,
                                            "Acceda a su correo y busque un correo con " +
                                                    "asunto: Restablece tu contraseña.",
                                            Snackbar.LENGTH_INDEFINITE
                                        ).setAction("Aceptar") { }.show()
                                    Constants.Status.Failure -> dialogOops =
                                        it.exception!!.setError(
                                            childFragmentManager,
                                            LogInFragment::class.java.name
                                        )
                                    else -> {}
                                }
                            }
                    }
                }
            }
        }
    }

}