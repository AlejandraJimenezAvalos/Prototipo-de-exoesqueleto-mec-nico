package com.example.exoesqueletov1.ui.fragments.singin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.utils.Utils.confirmPassword
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getText
import com.example.exoesqueletov1.utils.Utils.isNotEmpty
import com.example.exoesqueletov1.utils.Utils.isValidEmail
import com.example.exoesqueletov1.utils.Utils.isValidPassword
import com.example.exoesqueletov1.utils.Utils.setError
import com.example.exoesqueletov1.databinding.SingInFragmentBinding
import com.example.exoesqueletov1.ui.dialogs.DialogLoading

class SingInFragment : Fragment() {

    private lateinit var binding: SingInFragmentBinding
    private lateinit var viewModel: SingInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SingInViewModel::class.java]
        this.binding = SingInFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSingIn.setOnClickListener {
                val emailState = layoutEmail.isNotEmpty("Debe introducir su correo")
                val passwordState = layoutPassword.isNotEmpty("Debe introducir su contraseña")
                val confirmPasswordState = layoutConfirmPassword.isNotEmpty("Repita su contraseña")
                if (emailState && passwordState && confirmPasswordState) {
                    val emailValidation = layoutEmail.isValidEmail()
                    val passwordValidation = layoutPassword.isValidPassword()
                    val passwordConfirm = layoutConfirmPassword.confirmPassword(layoutPassword)
                    val dialogLoading = DialogLoading()
                    if (emailValidation && passwordValidation && passwordConfirm) {
                        viewModel.singUp(layoutEmail.getText(), layoutPassword.getText())
                            .observe(viewLifecycleOwner) {
                                it.status.createLoadingDialog(
                                    childFragmentManager,
                                    SingInFragment::class.java.name,
                                    dialogLoading
                                )
                                if (it.status == Constants.Status.Failure) {
                                    it.exception!!.setError(
                                        childFragmentManager,
                                        SingInFragment::class.java.name
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}