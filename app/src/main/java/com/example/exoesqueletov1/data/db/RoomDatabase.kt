package com.example.exoesqueletov1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.models.UserModel

@Database(entities = [UserModel::class], version = 1)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun getDataDao(): Dao
}