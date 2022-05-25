package com.example.exoesqueletov1.domain

import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.UserModel
import javax.inject.Inject

class DataRepository @Inject constructor(private val dao: Dao) {
    fun insertUser(userModel: UserModel) = dao.insertUser(userModel)
    fun updateUser(userModel: UserModel) = dao.updateUser(userModel)
    fun getUser(id: String) = dao.getUser(id)
    fun deleteUser() = dao.deleteUser()

    fun insertProfile(profileModel: ProfileModel) = dao.insertProfile(profileModel)
    fun updateProfile(profileModel: ProfileModel) = dao.updateProfile(profileModel)
    fun getProfile(id: String) = dao.getProfile(id)
    fun deleteProfile() = dao.deleteProfile()
    fun singOut() {
        dao.deleteProfile()
        dao.deleteUser()
    }
}