package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.postura

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.models.consultation.EvaluacionPostura
import com.example.exoesqueletov1.databinding.ItemPosturaBinding

class PosturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemPosturaBinding.bind(itemView)
    fun bind(postura: EvaluacionPostura) {
        binding.postura = postura
        binding.textGrados.text = postura.grados.toString()
    }
}