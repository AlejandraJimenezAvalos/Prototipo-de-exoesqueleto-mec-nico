package com.example.exoesqueletov1.ui.fragments.add_patient.adapter

import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.ExpedientModel
import com.example.exoesqueletov1.databinding.ItemTwoFieldBinding

class ExpedientViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemTwoFieldBinding.bind(view)

    fun bind(expedient: ExpedientModel) {
        binding.data = expedient
        binding.item.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.fab_scale_up))
    }
}