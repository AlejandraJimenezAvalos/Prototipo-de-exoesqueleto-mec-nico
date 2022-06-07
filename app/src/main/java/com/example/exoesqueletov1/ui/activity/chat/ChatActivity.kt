package com.example.exoesqueletov1.ui.activity.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.exoesqueletov1.data.models.MessageModel
import com.example.exoesqueletov1.databinding.ActivityChatBinding
import com.example.exoesqueletov1.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        setContentView(binding.root)

        val list = mutableListOf<MessageModel>()

        val idUser = savedInstanceState!!.getString(Constants.ID)!!
        viewModel.getMessages(idUser).observe(this) {
            list.addAll(it)
        }
    }
}