package com.example.exoesqueletov1.ui.fragments.login.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.local.entity.UsersEntity

class UsersAdapter(
    private val list: List<UsersEntity>,
    private val user: (UsersEntity, Constants.ActionUsers) -> Unit
) : RecyclerView.Adapter<UsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UsersViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false),
        user
    )

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}