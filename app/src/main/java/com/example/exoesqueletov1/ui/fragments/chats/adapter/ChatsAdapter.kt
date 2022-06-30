package com.example.exoesqueletov1.ui.fragments.chats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.PatientModel

class ChatsAdapter(
    private val list: MutableList<PatientModel>,
    private val onClick: (PatientModel) -> Unit
) :
    RecyclerView.Adapter<ChatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatsViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}