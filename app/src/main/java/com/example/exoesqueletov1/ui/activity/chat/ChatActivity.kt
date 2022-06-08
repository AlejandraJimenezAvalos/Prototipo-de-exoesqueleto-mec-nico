package com.example.exoesqueletov1.ui.activity.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exoesqueletov1.data.models.MessageModel
import com.example.exoesqueletov1.databinding.ActivityChatBinding
import com.example.exoesqueletov1.ui.activity.chat.adapter.ChatAdapter
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.example.exoesqueletov1.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: ChatViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        setContentView(binding.root)

        val idUser = intent!!.extras!!.getString(Constants.ID, "")
        val list = mutableListOf<MessageModel>()
        val adapter = ChatAdapter(list)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true

        binding.recyclerChatsActivity.setHasFixedSize(true)
        binding.recyclerChatsActivity.layoutManager = linearLayoutManager
        binding.recyclerChatsActivity.adapter = adapter

        viewModel.result.observe(this) {
            if (it.status == Constants.Status.Failure) {
                val dialogOops = DialogOops(it.exception!!.message)
                dialogOops.show(supportFragmentManager, ChatActivity::class.java.name)
            }
            if (it.status == Constants.Status.Success)
                binding.textActivityChatName.text = it.data!!.name
        }

        viewModel.getMessages(idUser).observe(this) {
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

        binding.imageViewSend.setOnClickListener {
            binding.editTextMessage.isValidMessage(idUser) {
                viewModel.setMessage(it)
            }
        }
    }

}

private fun TextInputEditText.isValidMessage(idUser: String, message: (MessageModel) -> Unit) {
    if (text.toString().isNotEmpty()) {
        message.invoke(
            MessageModel(
                UUID.randomUUID().toString(),
                Date().toString(),
                Firebase.auth.currentUser!!.uid,
                idUser,
                text.toString().trim(),
                false
            )
        )
        setText("")
    }
}
