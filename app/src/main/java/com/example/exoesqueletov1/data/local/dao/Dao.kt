package com.example.exoesqueletov1.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userModel: UserModel)

    @Update
    fun updateUser(userModel: UserModel)

    @Query("SELECT * FROM user WHERE id == :id")
    fun getUser(id: String): LiveData<UserModel>

    @Query("DELETE FROM user")
    fun deleteUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profileModel: ProfileModel)

    @Update
    fun updateProfile(profileModel: ProfileModel)

    @Query("SELECT * FROM profile WHERE id == :id")
    fun getProfile(id: String): LiveData<ProfileModel>

    @Query("DELETE FROM profile")
    fun deleteProfile()
}