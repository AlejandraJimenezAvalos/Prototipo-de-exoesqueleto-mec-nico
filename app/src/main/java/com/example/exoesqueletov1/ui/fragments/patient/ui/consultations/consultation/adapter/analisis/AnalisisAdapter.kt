package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.analisis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.consultation.Analisis

class AnalisisAdapter(private val list: List<Analisis>) :
    RecyclerView.Adapter<AnalisisViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AnalisisViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_analisis, parent, false)
    )

    override fun onBindViewHolder(holder: AnalisisViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.divider.visibility =
            if (list.lastIndex == position) View.GONE else View.VISIBLE
    }

    override fun getItemCount() = list.size
}