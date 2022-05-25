package com.example.exoesqueletov1.ui.activity.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.firebase.Constants

class MenuAdapter(
    private val listItem: List<Constants.Menu>,
    private val listener: (Constants.Menu) -> Unit
) : RecyclerView.Adapter<MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MenuViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false),
            listener
        )

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount() = listItem.size
}