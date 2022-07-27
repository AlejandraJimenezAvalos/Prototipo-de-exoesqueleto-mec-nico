package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.musculo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMusculo

class MusculoAdapter(private val list: List<EvaluacionMusculo>) :
    RecyclerView.Adapter<MusculoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MusculoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_musculo, parent, false)
    )

    override fun onBindViewHolder(holder: MusculoViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.divider.visibility =
            if (list.lastIndex == position) View.GONE else View.VISIBLE
    }

    override fun getItemCount() = list.size
}