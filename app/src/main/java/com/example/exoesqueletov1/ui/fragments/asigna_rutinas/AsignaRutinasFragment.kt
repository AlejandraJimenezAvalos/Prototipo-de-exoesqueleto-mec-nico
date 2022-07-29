package com.example.exoesqueletov1.ui.fragments.asigna_rutinas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.R

class AsignaRutinasFragment : Fragment() {

    companion object {
        fun newInstance() = AsignaRutinasFragment()
    }

    private lateinit var viewModel: AsignaRutinasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_asigna_rutinas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AsignaRutinasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}