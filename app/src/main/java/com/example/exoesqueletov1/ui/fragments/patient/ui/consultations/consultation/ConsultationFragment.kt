package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.models.consultation.Analisis
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMusculo
import com.example.exoesqueletov1.data.models.consultation.EvaluacionPostura
import com.example.exoesqueletov1.databinding.FragmentConsultationBinding
import com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.analisis.AnalisisAdapter
import com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.musculo.MusculoAdapter
import com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.postura.PosturaAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsultationFragment : Fragment() {

    private lateinit var viewModel: ConsultationViewModel
    private lateinit var binding: FragmentConsultationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsultationBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[ConsultationViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listMusculos = mutableListOf<EvaluacionMusculo>()
        val listAnalisis = mutableListOf<Analisis>()
        val listPostura = mutableListOf<EvaluacionPostura>()
        val adapterMusculos = MusculoAdapter(listMusculos)
        val adapterAnalisis = AnalisisAdapter(listAnalisis)
        val adapterPostura = PosturaAdapter(listPostura)
        binding.recyclerEvaluacionMuscular.adapter = adapterMusculos
        binding.recyclerAnalisis.adapter = adapterAnalisis
        binding.recyclerPostura.adapter = adapterPostura
        binding.recyclerEvaluacionMuscular.setHasFixedSize(true)
        binding.recyclerAnalisis.setHasFixedSize(true)
        binding.recyclerPostura.setHasFixedSize(true)
        viewModel.consultation.observe(viewLifecycleOwner) {
            val data = Data(it)
            binding.consultationRelation = it
            binding.data = data
            listMusculos.clear()
            listAnalisis.clear()
            listPostura.clear()
            if (it.evaluacionMuscular!!.evaluacionMusculo!!.isNotEmpty())
                listMusculos.addAll(it.evaluacionMuscular.evaluacionMusculo!!)
            if (it.marcha!!.analisis!!.isNotEmpty())
                it.marcha.analisis!!.forEach { analisis ->
                    if (analisis.valor) listAnalisis.add(analisis)
                }
            if (it.evaluacionPostura!!.isNotEmpty()) listPostura.addAll(it.evaluacionPostura)
            adapterMusculos.notifyDataSetChanged()
            adapterAnalisis.notifyDataSetChanged()
            adapterPostura.notifyDataSetChanged()
        }
    }

}