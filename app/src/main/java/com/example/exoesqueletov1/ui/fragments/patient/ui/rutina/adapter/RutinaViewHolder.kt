package com.example.exoesqueletov1.ui.fragments.patient.ui.rutina.adapter

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.models.rutina.RutinaModel
import com.example.exoesqueletov1.databinding.ItemRutinasBinding
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getConclusion
import com.example.exoesqueletov1.utils.Utils.startEndAnimation
import com.example.exoesqueletov1.utils.Utils.startFirstAnimation

class RutinaViewHolder(
    view: View,
    private val function: (RutinaModel) -> Unit,
    private val activity: Activity
) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemRutinasBinding.bind(view)

    fun bind(rutinaModel: RutinaModel) {
        binding.rutina = rutinaModel

        when (rutinaModel.finalize.getConclusion()) {
            Constants.Finalize.New -> {
                binding.buttonSave.visibility = View.VISIBLE
                binding.buttonSave.startFirstAnimation(activity)
            }
            Constants.Finalize.Start -> {
                binding.constraintAdd.startEndAnimation(activity)
                binding.constraintStart.startFirstAnimation(activity)
                binding.constraintActions.visibility = View.VISIBLE
                binding.buttonAdd.visibility = View.GONE
            }
            Constants.Finalize.Finalize -> {
                binding.constraintAdd.startEndAnimation(activity)
                binding.constraintStart.startFirstAnimation(activity)
                binding.constraintResumen.visibility = View.VISIBLE
                binding.buttonAdd.visibility = View.GONE
            }
        }

        binding.buttonAdd.setOnClickListener {
            binding.buttonAdd.startEndAnimation(activity)
            binding.constraintAdd.startFirstAnimation(activity)
        }
        binding.buttonSave.setOnClickListener {
            if (rutinaModel.isValid()) {
                rutinaModel.finalize = Constants.Finalize.Start.toString()
                function.invoke(rutinaModel)
            } else Toast.makeText(
                itemView.context,
                "Introduce la información correspondiente.",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonCancel.setOnClickListener {
            binding.constraintAdd.startEndAnimation(activity)
            binding.buttonAdd.startFirstAnimation(activity)
        }
        binding.buttonCaminata.setOnClickListener {
            binding.textModo.visibility = View.VISIBLE
            binding.groupModo.visibility = View.VISIBLE
            binding.textTipo.visibility = View.GONE
            binding.slider.visibility = View.GONE
            binding.textAlternaciones.visibility = View.GONE
            binding.groupAlternaciones.visibility = View.GONE
            binding.textUnidades.visibility = View.GONE
            binding.textTipoUnidades.visibility = View.GONE
            rutinaModel.type = Constants.Type.Caminata.toString()
            rutinaModel.modo = Constants.Modo.Null.toString()
        }
        binding.buttonRepeticiones.setOnClickListener {
            binding.textTipo.text = "Número de repeticiones"
            binding.slider.valueFrom = 3f
            binding.slider.valueTo = 15f
            binding.slider.value = 5f
            binding.slider.stepSize = 1f
            binding.textUnidades.text = "5"
            binding.textTipoUnidades.text = "Repeticiones"
            binding.textUnidades.visibility = View.VISIBLE
            binding.textTipoUnidades.visibility = View.VISIBLE
            binding.textModo.visibility = View.GONE
            binding.groupModo.visibility = View.GONE
            binding.textTipo.visibility = View.VISIBLE
            binding.slider.visibility = View.VISIBLE
            binding.textAlternaciones.visibility = View.VISIBLE
            binding.textAlternaciones.visibility = View.VISIBLE
            binding.groupAlternaciones.visibility = View.VISIBLE
            rutinaModel.type = Constants.Type.Repeticiones.toString()
            rutinaModel.modo = Constants.Modo.Null.toString()
        }

        binding.buttonPasos.setOnClickListener {
            binding.textTipo.text = "Número de pasos"
            binding.textTipo.visibility = View.VISIBLE
            binding.slider.visibility = View.VISIBLE
            binding.slider.valueFrom = 5f
            binding.slider.valueTo = 20f
            binding.slider.value = 5f
            binding.slider.stepSize = 1f
            binding.textUnidades.text = "5"
            binding.textTipoUnidades.text = "Pasos"
            binding.textUnidades.visibility = View.VISIBLE
            binding.textTipoUnidades.visibility = View.VISIBLE
            rutinaModel.modo = Constants.Modo.Pasos.toString()
        }

        binding.buttonTiempo.setOnClickListener {
            binding.textTipo.text = "Tiempo que durará la caminata"
            binding.textTipo.visibility = View.VISIBLE
            binding.slider.visibility = View.VISIBLE
            binding.slider.valueFrom = 1f
            binding.slider.valueTo = 5f
            binding.slider.value = 1f
            binding.slider.stepSize = 1f
            binding.textUnidades.text = "1"
            binding.textTipoUnidades.text = "Minutos"
            binding.textUnidades.visibility = View.VISIBLE
            binding.textTipoUnidades.visibility = View.VISIBLE
            rutinaModel.modo = Constants.Modo.Minutos.toString()
        }

        binding.slider.addOnChangeListener { _, value, _ ->
            binding.textUnidades.text = value.toInt().toString()
            rutinaModel.value = value.toInt().toString()
        }

        binding.buttonDerecha.setOnClickListener {
            rutinaModel.modo = Constants.Modo.Derecha.toString()
        }

        binding.buttonIzquierda.setOnClickListener {
            rutinaModel.modo = Constants.Modo.Izquierda.toString()
        }

        binding.buttonAmbos.setOnClickListener {
            rutinaModel.modo = Constants.Modo.Ambos.toString()
        }

    }

}