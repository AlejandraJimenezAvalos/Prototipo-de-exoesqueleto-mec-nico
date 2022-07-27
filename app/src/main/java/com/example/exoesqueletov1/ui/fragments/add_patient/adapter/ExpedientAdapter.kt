package com.example.exoesqueletov1.ui.fragments.add_patient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.ExpedientModel

class ExpedientAdapter(val list: List<ExpedientModel>) :

    RecyclerView.Adapter<ExpedientViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpedientViewHolder =
        ExpedientViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_expedient, parent, false)
        )

    override fun onBindViewHolder(holder: ExpedientViewHolder, position: Int) =
        holder.bind(list[position])


    override fun getItemCount() = list.size
}