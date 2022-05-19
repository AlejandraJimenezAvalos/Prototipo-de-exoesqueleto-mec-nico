package com.example.exoesqueletov1.domain

import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.models.UserModel
import javax.inject.Inject

class DataRepository @Inject constructor(private val dao: Dao) {
    fun insertUser(userModel: UserModel) = dao.insertUser(userModel)
    fun updateUser(userModel: UserModel) = dao.update(userModel)
    fun getUser(id: String) = dao.getUser(id)
    fun deleteUser() = dao.deleteUser()
}