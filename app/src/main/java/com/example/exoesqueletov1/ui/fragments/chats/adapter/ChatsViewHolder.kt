package com.example.exoesqueletov1.ui.fragments.chats.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.models.PatientModel
import com.example.exoesqueletov1.databinding.ItemChatBinding
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Utils.getOrigin

class ChatsViewHolder(view: View, private val chat: (PatientModel) -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val binding = ItemChatBinding.bind(view)

    fun bind(patientModel: PatientModel) {
        binding.chat = patientModel
        if (patientModel.origin.getOrigin() == Constants.Origin.Create)
            binding.imageChat.visibility = View.GONE
        binding.imageChat.setOnClickListener {
            chat.invoke(patientModel)
        }
        binding.imageAdd.setOnClickListener {

        }
        binding.imageWalk.setOnClickListener {

        }
        binding.imageProceedings.setOnClickListener {

        }
    }
}