package com.example.exoesqueletov1.ui.fragments.patient.ui.rutina

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.models.rutina.RutinaModel
import com.example.exoesqueletov1.databinding.FragmentRutinaBinding
import com.example.exoesqueletov1.ui.fragments.patient.ui.rutina.adapter.RutinaAdapter
import com.example.exoesqueletov1.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RutinasFragment(private val function: (RutinaModel) -> Unit) : Fragment() {

    private lateinit var binding: FragmentRutinaBinding
    private lateinit var viewModel: RutinaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRutinaBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[RutinaViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var blank = RutinaModel(
            UUID.randomUUID().toString(),
            Constants.Type.Null.toString(),
            Constants.Modo.Null.toString(),
            "0",
            Constants.Finalize.New.toString(),
            "",
            "",
        )
        val listRutinas = mutableListOf(blank)
        val adapter = RutinaAdapter(listRutinas, requireActivity(), function) { rutina, state ->
            if (state) viewModel.insertRutina(rutina)
            else viewModel.deleteRutina(rutina)
        }
        binding.recyclerRutina.setHasFixedSize(true)
        binding.recyclerRutina.adapter = adapter
        viewModel.listRutinas.observe(viewLifecycleOwner) {
            blank = RutinaModel(
                UUID.randomUUID().toString(),
                Constants.Type.Null.toString(),
                Constants.Modo.Null.toString(),
                "0",
                Constants.Finalize.New.toString(),
                "",
                "",
            )
            adapter.list.clear()
            adapter.list.addAll(it)
            adapter.list.add(blank)
            adapter.notifyDataSetChanged()
        }
    }

}