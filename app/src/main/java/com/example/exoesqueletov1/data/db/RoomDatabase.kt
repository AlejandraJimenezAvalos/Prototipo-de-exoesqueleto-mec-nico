package com.example.exoesqueletov1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel

@Database(entities = [UserModel::class, ProfileModel::class], version = 1)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun getDataDao(): Dao
}