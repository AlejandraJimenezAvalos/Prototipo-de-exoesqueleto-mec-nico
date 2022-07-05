package com.example.exoesqueletov1.domain

import androidx.lifecycle.LiveData
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.models.*
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
        dao.deleteExoskeleton()
        dao.deletePatients()
        dao.deleteExpedints()
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
    fun readMessage(idUser: String) = dao.readMessages(idUser)

    fun insertChat(chatsEntity: ChatsEntity) = dao.insertChat(chatsEntity)
    fun getChats() = dao.getChats()
    fun getChat(idUser: String) = dao.getChat(idUser)

    fun insertExoskeleton(exoskeletonModel: ExoskeletonModel) =
        dao.insertExoskeleton(exoskeletonModel)

    fun getExoskeleton() = dao.getExoskeleton()

    fun insertPatient(patientModel: PatientModel) = dao.insertPatient(patientModel)
    fun getPatients(): LiveData<List<PatientModel>> = dao.getPatients()
    fun getPatient(id: String): LiveData<PatientModel> = dao.getPatient(id)
    fun deletePatients() = dao.deletePatients()

    fun getExpedient() = dao.getExpedient()
    fun setExpedients(list: List<ExpedientModel>) = list.forEach { dao.setExpedient(it) }
    fun setExpedient(expedientModel: ExpedientModel) = dao.setExpedient(expedientModel)

}