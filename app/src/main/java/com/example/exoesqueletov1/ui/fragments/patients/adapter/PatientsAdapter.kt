package com.example.exoesqueletov1.ui.fragments.patients.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.PatientModel

class PatientsAdapter(
    private val list: MutableList<PatientModel>,
    private val onClick: (PatientModel) -> Unit
) : RecyclerView.Adapter<PatientsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PatientsViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: PatientsViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size
}