package com.example.exoesqueletov1.ui.activity.main.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.databinding.ItemMenuBinding

class MenuViewHolder(itemView: View, private val listener: (Constants.Menu) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val binding: ItemMenuBinding = ItemMenuBinding.bind(itemView)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(menu: Constants.Menu) {
        binding.imgUser.setImageDrawable(itemView.resources.getDrawable(when (menu) {
            Constants.Menu.Notification -> R.drawable.ic_notification
            Constants.Menu.Profile -> R.drawable.ic_profile
            Constants.Menu.Chats -> R.drawable.ic_message
            Constants.Menu.Control -> R.drawable.ic_bluetooth
            Constants.Menu.WorkSpecialist -> R.drawable.ic_work
        }, null))
        binding.imgUser.setOnClickListener { listener.invoke(menu) }
    }
}