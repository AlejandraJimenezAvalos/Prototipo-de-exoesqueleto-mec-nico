package com.example.exoesqueletov1.ui.fragments.chats.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.databinding.ItemChatBinding

class ChatsViewHolder(view: View, private val chat: (ChatsEntity) -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val binding = ItemChatBinding.bind(view)

    fun bind(chatsEntity: ChatsEntity) {
        binding.chat = chatsEntity
        binding.itemChat.setOnClickListener {
            chat.invoke(chatsEntity)
        }
    }
}