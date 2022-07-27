package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation.adapter.postura

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.consultation.EvaluacionPostura

class PosturaAdapter(private val list: List<EvaluacionPostura>) :
    RecyclerView.Adapter<PosturaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PosturaViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_postura, parent, false)
    )

    override fun onBindViewHolder(holder: PosturaViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.divider.visibility =
            if (list.lastIndex == position) View.GONE else View.VISIBLE
    }

    override fun getItemCount() = list.size
}