package com.example.exoesqueletov1.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants

@Entity(tableName = "users")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.EMAIL) val email: String,
    @ColumnInfo(name = Constants.NAME) var name: String,
    @ColumnInfo(name = Constants.PASSWORD) val password: String,
    @ColumnInfo(name = Constants.PHOTO_PATH) var photoPath: String
)
