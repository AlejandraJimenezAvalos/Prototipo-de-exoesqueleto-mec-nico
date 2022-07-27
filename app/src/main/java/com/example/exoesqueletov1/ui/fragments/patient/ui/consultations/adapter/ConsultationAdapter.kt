package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.consultation.ConsultationData

class ConsultationAdapter(
    private val list: List<ConsultationData>,
    private val function: (ConsultationData) -> Unit
) :
    RecyclerView.Adapter<ConsultationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConsultationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_consultation, parent, false),
            function
        )

    override fun onBindViewHolder(holder: ConsultationViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size
}