package com.example.exoesqueletov1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.models.MessageModel
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel

@Database(
    entities = [
        UserModel::class,
        ProfileModel::class,
        UsersEntity::class,
        MessageModel::class,
        ChatsEntity::class
    ],
    version = 1
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun getDataDao(): Dao
}