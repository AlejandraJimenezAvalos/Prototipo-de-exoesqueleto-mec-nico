package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.analisis

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.models.consultation.Analisis
import com.example.exoesqueletov1.databinding.ItemAnalisisBinding

class AnalisisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemAnalisisBinding.bind(itemView)
    fun bind(analisis: Analisis) {
        binding.analisis = analisis
    }
}