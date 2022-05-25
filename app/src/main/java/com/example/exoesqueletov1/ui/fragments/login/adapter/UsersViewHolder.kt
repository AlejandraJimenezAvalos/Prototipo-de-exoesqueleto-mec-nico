package com.example.exoesqueletov1.ui.fragments.login.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.databinding.ItemUserBinding

class UsersViewHolder(
    itemView: View,
    private val user: (UsersEntity, Constants.ActionUsers) -> Unit
) :
    RecyclerView.ViewHolder(itemView) {
    private val binding = ItemUserBinding.bind(itemView)

    fun bind(usersEntity: UsersEntity) {
        binding.user = usersEntity
        binding.cardUser.setOnClickListener {
            user.invoke(usersEntity, Constants.ActionUsers.LogIn)
        }
        binding.buttonDelete.setOnClickListener {
            user.invoke(usersEntity, Constants.ActionUsers.Delete)
        }
    }
}