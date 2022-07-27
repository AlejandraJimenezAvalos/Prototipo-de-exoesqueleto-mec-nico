package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.adapter

import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.consultation.ConsultationData
import com.example.exoesqueletov1.databinding.ItemConsultationBinding

class ConsultationViewHolder(itemView: View, private val function: (ConsultationData) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val binding = ItemConsultationBinding.bind(itemView)

    fun bind(consultationData: ConsultationData) {
        binding.consultation = consultationData
        binding.item.startAnimation(
            AnimationUtils.loadAnimation(
                itemView.context,
                R.anim.fab_scale_up
            )
        )
        binding.item.setOnClickListener {
            function.invoke(consultationData)
        }
    }
}