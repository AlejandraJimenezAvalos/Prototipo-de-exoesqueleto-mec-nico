package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.musculo

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMusculo
import com.example.exoesqueletov1.databinding.ItemMusculoBinding

class MusculoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemMusculoBinding.bind(itemView)
    fun bind(evaluacionMusculo: EvaluacionMusculo) {
        binding.musculo = evaluacionMusculo
        binding.textValor.text = when (evaluacionMusculo.valor) {
            0 -> "Ausencia de contracción muscular."
            1 -> "Esbozo de contracción muscular."
            2 -> "Movimiento activo que no puede vencer la fuerza de gavedad. Por " +
                    "ejemplo, el codo puede flexionarse completamente solo cuando el " +
                    "brazo es mantenido en un plano horizontal."
            3 -> "La fuerza muscular está reducida tanto que el movimiento " +
                    "articular solo puede realizarse contra la gravedad, sin la " +
                    "resistencia del examinador. Por ejemplo, la articulación del " +
                    "codo puede moverse desde extensión completa hasta flexión " +
                    "completa, comenzando con el brazo suspendido al lado del cuerpo."
            4 -> "La fuerza muscular esta reducida, pero la contracción muscular " +
                    "puede realizar un movimiento articular contra la resistencia."
            5 -> "Fuerza muscular normal contra la resistencia completa."
            else -> "Ausencia de contracción muscular."
        }
        binding.textValorNumero.text = evaluacionMusculo.valor.toString()
    }
}