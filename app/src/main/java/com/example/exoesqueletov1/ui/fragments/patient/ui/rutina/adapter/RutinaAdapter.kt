package com.example.exoesqueletov1.ui.fragments.patient.ui.rutina.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.rutina.RutinaModel

class RutinaAdapter(
    val list: MutableList<RutinaModel>,
    private val activity: Activity,
    private val functionGo: (RutinaModel) -> Unit,
    private val function: (RutinaModel, Boolean) -> Unit,
) :
    RecyclerView.Adapter<RutinaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RutinaViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rutinas, parent, false),
        function,
        functionGo,
        activity
    )

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        holder.bind(list[position]) {
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = list.size
}