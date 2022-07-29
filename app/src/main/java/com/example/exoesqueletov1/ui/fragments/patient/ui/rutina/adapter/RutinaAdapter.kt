package com.example.exoesqueletov1.ui.fragments.patient.ui.rutina.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.rutina.RutinaModel

class RutinaAdapter(
    private val list: MutableList<RutinaModel>,
    private val function: (RutinaModel) -> Unit,
    private val activity: Activity
) :
    RecyclerView.Adapter<RutinaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RutinaViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rutinas, parent, false),
        function,
        activity
    )

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}