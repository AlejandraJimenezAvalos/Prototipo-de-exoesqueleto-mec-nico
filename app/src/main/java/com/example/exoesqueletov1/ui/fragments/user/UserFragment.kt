package com.example.exoesqueletov1.ui.fragments.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.databinding.FragmentUserBinding
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.fragments.NotificationFragment
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getUser
import com.example.exoesqueletov1.utils.Utils.isNotEmpty
import com.example.exoesqueletov1.utils.Utils.setError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var loading: DialogLoading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        loading = DialogLoading()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSaveProfile.setOnClickListener {
                val nameState = editTextNombre.isNotEmpty("No puedes dejar el campo en blanco.")
                val lastNameState =
                    editTextApellidos.isNotEmpty("No puedes dejar el campo en blanco.")
                val dateState = editTextDate.isNotEmpty("No puedes dejar el campo en blanco.")
                val countryState =
                    spinnerContry.isNotEmpty(spinnerCountry, "Debe seleccionar una opción.")
                if (nameState && lastNameState && dateState && countryState) {
                    viewModel.saveUser(getUser()).observe(viewLifecycleOwner) {
                        it.status.createLoadingDialog(
                            parentFragmentManager,
                            UserFragment::class.java.name,
                            loading
                        )
                        if (it.status == Constants.Status.Success)
                            parentFragmentManager.beginTransaction().replace(
                                R.id.container_main, NotificationFragment()
                            ).commit()
                        if (it.status == Constants.Status.Failure)
                            it.exception!!.setError(
                                parentFragmentManager,
                                UserFragment::class.java.name,
                            )
                    }
                }
            }
        }
    }

}