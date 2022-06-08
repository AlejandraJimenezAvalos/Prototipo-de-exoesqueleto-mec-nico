package com.example.exoesqueletov1.ui.activity.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.models.MessageModel
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(
    private val itemList: List<MessageModel>,
) : RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ChatViewHolder(
        if (viewType == CODE_MY_MESSAGE) {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_my_message, viewGroup, false)
        } else {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_your_message, viewGroup, false)
        }
    )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.textViewMessage.text = itemList[position].message
        holder.textViewDate.text = itemList[position].date
    }

    override fun getItemViewType(position: Int) =
        if (itemList[position].from == FirebaseAuth.getInstance().currentUser!!.uid) {
            CODE_MY_MESSAGE
        } else
            CODE_YOUR_MESSAGE

    override fun getItemCount() = itemList.size

    companion object {
        private const val CODE_MY_MESSAGE = 0
        private const val CODE_YOUR_MESSAGE = 1
    }
}