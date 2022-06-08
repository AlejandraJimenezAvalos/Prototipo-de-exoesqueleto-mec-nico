package com.example.exoesqueletov1.ui.activity.chat.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewMessage: TextView
    val textViewDate: TextView

    init {
        textViewMessage = itemView.findViewById(R.id.textView_message)
        textViewDate = itemView.findViewById(R.id.textView_hour)
    }
}