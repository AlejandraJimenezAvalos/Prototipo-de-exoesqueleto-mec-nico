package com.example.exoesqueletov1.domain

import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.models.MessageModel
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
        dao.deleteChats()
        dao.deleteMessages()
    }

    fun insertNewUser(usersEntity: UsersEntity) = dao.insertNewUser(usersEntity)
    fun getUsers() = dao.getUsers()
    fun deleteUsers(id: String) = dao.deleteUsers(id)
    fun deleteEverything() = dao.deleteEverything()
    fun getUsers(id: String) = dao.getUsers(id)
    fun updateUsers(usersEntity: UsersEntity) = dao.updateUsers(usersEntity)

    fun insertMessage(messageModel: MessageModel) = dao.insertMessage(messageModel)
    fun getGroups(id: String) = dao.getGroups(id)
    fun getMessages(id: String) = dao.getMessages(id)
    fun getMessage(id: String) = dao.getMessage(id)

    fun insertChat(chatsEntity: ChatsEntity) = dao.insertChat(chatsEntity)
    fun getChats() = dao.getChats()
}