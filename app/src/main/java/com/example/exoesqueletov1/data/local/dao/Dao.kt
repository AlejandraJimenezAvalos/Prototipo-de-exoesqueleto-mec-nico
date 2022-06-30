package com.example.exoesqueletov1.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.data.local.query.GroupsQuery
import com.example.exoesqueletov1.data.models.*

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userModel: UserModel)

    @Update
    fun updateUser(userModel: UserModel)

    @Query("SELECT * FROM user WHERE id == :id LIMIT 1")
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUser(usersEntity: UsersEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Query("SELECT * FROM users WHERE id == :id")
    fun getUsers(id: String): LiveData<UsersEntity>

    @Update
    fun updateUsers(usersEntity: UsersEntity)

    @Query("DELETE FROM users WHERE id == :id")
    fun deleteUsers(id: String)

    @Query("DELETE FROM users")
    fun deleteEverything()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messageModel: MessageModel)

    @Query("SELECT DISTINCT `from`, `to` FROM messages WHERE `from` == :id OR `to` == :id")
    fun getGroups(id: String): LiveData<List<GroupsQuery>>

    @Query("SELECT DISTINCT * FROM messages WHERE `from` == :id OR `to` == :id ORDER BY DATE(date) ASC")
    fun getMessages(id: String): LiveData<List<MessageModel>>

    @Query("SELECT id, date, `from`, `to`, message, 1 AS status FROM MESSAGES WHERE `from` == :idUser and status = 0")
    fun readMessages(idUser: String): LiveData<List<MessageModel>>

    @Query("SELECT * FROM messages WHERE `from` == :id OR `to` == :id ORDER BY date DESC LIMIT 1")
    fun getMessage(id: String): LiveData<MessageModel>

    @Query("DELETE FROM messages")
    fun deleteMessages()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chatsEntity: ChatsEntity)

    @Query("SELECT * FROM chats")
    fun getChats(): LiveData<List<ChatsEntity>>

    @Query("SELECT * FROM chats WHERE userId == :idUser LIMIT 1")
    fun getChat(idUser: String): LiveData<ChatsEntity>

    @Query("DELETE FROM chats")
    fun deleteChats()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExoskeleton(exoskeletonModel: ExoskeletonModel)

    @Query("SELECT id AS id, mac AS mac, userId AS userId, (SELECT name FROM user WHERE id == userId) AS name, 'Emparejado' AS status FROM exoskeleton")
    fun getExoskeleton(): LiveData<List<ExoskeletonQuery>>

    @Query("DELETE FROM exoskeleton")
    fun deleteExoskeleton()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatient(patientModel: PatientModel)

    @Query("SELECT * FROM patient")
    fun getPatients(): LiveData<List<PatientModel>>

    @Query("SELECT * FROM patient WHERE id == :id")
    fun getPatient(id: String): LiveData<PatientModel>

    @Query("DELETE FROM PATIENT")
    fun deletePatients()

}