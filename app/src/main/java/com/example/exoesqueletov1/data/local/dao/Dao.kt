package com.example.exoesqueletov1.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.exoesqueletov1.data.models.UserModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userModel: UserModel)

    @Update
    fun update(userModel: UserModel)

    @Query("SELECT * FROM user WHERE id == :id")
    fun getUser(id: String): LiveData<UserModel>

    @Query("DELETE FROM user")
    fun deleteUser()
}