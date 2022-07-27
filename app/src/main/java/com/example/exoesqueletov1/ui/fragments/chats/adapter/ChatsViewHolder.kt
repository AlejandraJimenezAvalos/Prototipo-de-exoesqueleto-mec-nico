package com.example.exoesqueletov1.ui.fragments.chats.adapter

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.databinding.ItemChatBinding

class ChatsViewHolder(val view: View, private val chat: (PatientModel) -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val binding = ItemChatBinding.bind(view)

    fun bind(patientModel: PatientModel) {
        binding.chat = patientModel
        binding.itemChat.setOnClickListener {
            chat.invoke(patientModel)
            view.findNavController().navigate(R.id.action_navigation_message_to_patientFragment2)
        }
    }
}