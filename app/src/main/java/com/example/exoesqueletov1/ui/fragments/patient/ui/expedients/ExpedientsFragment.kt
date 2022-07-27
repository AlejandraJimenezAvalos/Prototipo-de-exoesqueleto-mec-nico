package com.example.exoesqueletov1.ui.fragments.patient.ui.expedients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.databinding.FragmentExpedientsBinding
import com.example.exoesqueletov1.ui.fragments.add_patient.adapter.ExpedientAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpedientsFragment : Fragment() {

    private lateinit var binding: FragmentExpedientsBinding
    private lateinit var viewModel: ExpedientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpedientsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ExpedientViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf<ExpedientModel>()
        val adapter = ExpedientAdapter(list)
        binding.recyclerExpedient.setHasFixedSize(true)
        binding.recyclerExpedient.adapter = adapter
        viewModel.expedients.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            Log.e("una prueba", list.toString())
            adapter.notifyDataSetChanged()
        }
    }

}