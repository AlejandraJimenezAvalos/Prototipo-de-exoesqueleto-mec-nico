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
    private val function: (RutinaModel, Boolean) -> Unit,
    private val functionGo: (RutinaModel) -> Unit,
    private val activity: Activity
) : RecyclerView.ViewHolder(view) {

    val binding = ItemRutinasBinding.bind(view)

    fun bind(rutinaModel: RutinaModel, deleteFunction: (Boolean) -> Unit) {
        binding.rutina = rutinaModel

        binding.apply {
            when (rutinaModel.finalize.getConclusion()) {
                Constants.Finalize.New -> {
                    buttonAdd.visibility = View.VISIBLE
                    buttonAdd.startFirstAnimation(activity)
                    binding.constraintStart.visibility = View.GONE
                }
                Constants.Finalize.Start -> {
                    constraintAdd.startEndAnimation(activity)
                    constraintStart.startFirstAnimation(activity)
                    constraintActions.visibility = View.VISIBLE
                    buttonAdd.visibility = View.GONE
                }
                Constants.Finalize.Finalize -> {
                    constraintAdd.startEndAnimation(activity)
                    constraintStart.startFirstAnimation(activity)
                    constraintResumen.visibility = View.VISIBLE
                    buttonAdd.visibility = View.GONE
                }
            }
            buttonAdd.setOnClickListener {
                buttonAdd.startEndAnimation(activity)
                constraintAdd.startFirstAnimation(activity)
            }

            buttonSave.setOnClickListener {
                if (rutinaModel.isValid()) {
                    rutinaModel.finalize = Constants.Finalize.Start.toString()
                    function.invoke(rutinaModel, true)
                } else Toast.makeText(
                    itemView.context,
                    "Introduce la información correspondiente.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            buttonCancel.setOnClickListener {
                constraintAdd.startEndAnimation(activity)
                buttonAdd.startFirstAnimation(activity)
            }

            buttonCaminata.setOnClickListener {
                textModo.visibility = View.VISIBLE
                groupModo.visibility = View.VISIBLE
                textTipo.visibility = View.GONE
                slider.visibility = View.GONE
                textAlternaciones.visibility = View.GONE
                groupAlternaciones.visibility = View.GONE
                textUnidades.visibility = View.GONE
                textTipoUnidades.visibility = View.GONE
                rutinaModel.type = Constants.Type.Caminata.toString()
                rutinaModel.modo = Constants.Modo.Null.toString()
            }

            buttonRepeticiones.setOnClickListener {
                textTipo.text = "Número de repeticiones"
                slider.valueFrom = 3f
                slider.valueTo = 15f
                slider.value = 5f
                slider.stepSize = 1f
                textUnidades.text = "5"
                textTipoUnidades.text = "Repeticiones"
                textUnidades.visibility = View.VISIBLE
                textTipoUnidades.visibility = View.VISIBLE
                textModo.visibility = View.GONE
                groupModo.visibility = View.GONE
                textTipo.visibility = View.VISIBLE
                slider.visibility = View.VISIBLE
                textAlternaciones.visibility = View.VISIBLE
                textAlternaciones.visibility = View.VISIBLE
                groupAlternaciones.visibility = View.VISIBLE
                rutinaModel.type = Constants.Type.Repeticiones.toString()
                rutinaModel.modo = Constants.Modo.Null.toString()
            }

            buttonDelete.setOnClickListener {
                function.invoke(rutinaModel, false)
                deleteFunction.invoke(true)
            }

            buttonStart.setOnClickListener {
                functionGo.invoke(rutinaModel)
            }

            buttonPasos.setOnClickListener {
                textTipo.text = "Número de pasos"
                textTipo.visibility = View.VISIBLE
                slider.visibility = View.VISIBLE
                slider.valueFrom = 5f
                slider.valueTo = 20f
                slider.value = 5f
                slider.stepSize = 1f
                textUnidades.text = "5"
                textTipoUnidades.text = "Pasos"
                textUnidades.visibility = View.VISIBLE
                textTipoUnidades.visibility = View.VISIBLE
                rutinaModel.modo = Constants.Modo.Pasos.toString()
            }

            buttonTiempo.setOnClickListener {
                textTipo.text = "Tiempo que durará la caminata"
                textTipo.visibility = View.VISIBLE
                slider.visibility = View.VISIBLE
                slider.valueFrom = 1f
                slider.valueTo = 5f
                slider.value = 1f
                slider.stepSize = 1f
                textUnidades.text = "1"
                textTipoUnidades.text = "Minutos"
                textUnidades.visibility = View.VISIBLE
                textTipoUnidades.visibility = View.VISIBLE
                rutinaModel.modo = Constants.Modo.Minutos.toString()
            }

            slider.addOnChangeListener { _, value, _ ->
                binding.textUnidades.text = value.toInt().toString()
                rutinaModel.value = value.toInt().toString()
            }

            buttonDerecha.setOnClickListener {
                rutinaModel.modo = Constants.Modo.Derecha.toString()
            }

            buttonIzquierda.setOnClickListener {
                rutinaModel.modo = Constants.Modo.Izquierda.toString()
            }

            buttonAmbos.setOnClickListener {
                rutinaModel.modo = Constants.Modo.Ambos.toString()
            }
        }

    }

}