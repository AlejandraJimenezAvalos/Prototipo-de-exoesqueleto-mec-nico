package com.example.exoesqueletov1.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants

@Entity(tableName = "chats")
data class ChatsEntity(
    @PrimaryKey(autoGenerate = false) val userId: String,
    @ColumnInfo(name = Constants.NAME) val name: String,
    @ColumnInfo(name = Constants.MESSAGE) val message: String,
)