package com.example.exoesqueletov1.data.models

import java.util.*

data class MessageModel(
    val id: String,
    val date: Date,
    val from: String,
    val to: String,
    val message: String,
)