package com.example.exoesqueletov1.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.OnConflictStrategy.REPLACE
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.data.local.query.GroupsQuery
import com.example.exoesqueletov1.data.local.relations.ConsultationRelation
import com.example.exoesqueletov1.data.models.*
import com.example.exoesqueletov1.data.models.consultation.*

@Dao
interface Dao {
    @Insert(onConflict = REPLACE)
    fun insertUser(userModel: UserModel)

    @Update
    fun updateUser(userModel: UserModel)

    @Query("SELECT * FROM user WHERE id == :id LIMIT 1")
    fun getUser(id: String): LiveData<UserModel>

    @Query("DELETE FROM user")
    fun deleteUser()

    @Insert(onConflict = REPLACE)
    fun insertProfile(profileModel: ProfileModel)

    @Update
    fun updateProfile(profileModel: ProfileModel)

    @Query("SELECT * FROM profile WHERE id == :id")
    fun getProfile(id: String): LiveData<ProfileModel>

    @Query("DELETE FROM profile")
    fun deleteProfile()

    @Insert(onConflict = REPLACE)
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

    @Insert(onConflict = REPLACE)
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

    @Insert(onConflict = REPLACE)
    fun insertChat(chatsEntity: ChatsEntity)

    @Query("SELECT * FROM chats")
    fun getChats(): LiveData<List<ChatsEntity>>

    @Query("SELECT * FROM chats WHERE userId == :idUser LIMIT 1")
    fun getChat(idUser: String): LiveData<ChatsEntity>

    @Query("DELETE FROM chats")
    fun deleteChats()

    @Insert(onConflict = REPLACE)
    fun insertExoskeleton(exoskeletonModel: ExoskeletonModel)

    @Query("SELECT id AS id, mac AS mac, userId AS userId, (SELECT name FROM user WHERE id == userId) AS name, 'Emparejado' AS status FROM exoskeleton")
    fun getExoskeleton(): LiveData<List<ExoskeletonQuery>>

    @Query("DELETE FROM exoskeleton")
    fun deleteExoskeleton()

    @Insert(onConflict = REPLACE)
    fun insertPatient(patientModel: PatientModel)

    @Query("SELECT * FROM patient")
    fun getPatients(): LiveData<List<PatientModel>>

    @Query("SELECT * FROM patient WHERE id == :id")
    fun getPatient(id: String): LiveData<PatientModel>

    @Query("DELETE FROM patient")
    fun deletePatients()

    @Query("SELECT * FROM expedient")
    fun getExpedient(): LiveData<List<ExpedientModel>>

    @Query("SELECT * FROM expedient WHERE idPatient == :id")
    fun getExpedients(id: String): LiveData<List<ExpedientModel>>

    @Insert(onConflict = REPLACE)
    fun setExpedient(expedientModel: ExpedientModel)

    @Query("DELETE FROM expedient")
    fun deleteExpedints()

    @Insert(onConflict = REPLACE)
    fun insertConsultation(consultationData: ConsultationData)

    @Query("DELETE FROM consultations")
    fun deleteConsultations()

    @Query("SELECT * FROM consultations WHERE idPatient == :idPatient")
    fun getConsultations(idPatient: String): LiveData<List<ConsultationData>>

    @Insert(onConflict = REPLACE)
    fun insertExploracionFisica(exploracionFisica: ExploracionFisica)

    @Query("DELETE FROM exploracion_fisica")
    fun deleteExploracionFisica()

    @Query("SELECT * FROM exploracion_fisica WHERE idConsult == :idConsult")
    fun getExploracionFisica(idConsult: String): LiveData<ExploracionFisica>

    @Insert(onConflict = REPLACE)
    fun insertEvaluacionPostura(evaluacionPostura: EvaluacionPostura)

    @Query("DELETE FROM evaluacion_postura")
    fun deleteEvaluacionPostura()

    @Query("SELECT * FROM evaluacion_postura WHERE idConsult == :idConsult")
    fun getEvaluacionPostura(idConsult: String): LiveData<List<EvaluacionPostura>>

    @Insert(onConflict = REPLACE)
    fun insertDiagnostico(diagnostico: Diagnostico)

    @Query("DELETE FROM diagnostico")
    fun deleteDiagnostico()

    @Query("SELECT * FROM diagnostico WHERE idConsult == :idConsult")
    fun getDiagnostico(idConsult: String): LiveData<Diagnostico>

    @Insert(onConflict = REPLACE)
    fun insertEvaluacionMuscular(evaluacionMuscular: EvaluacionMuscular)

    @Query("DELETE FROM evaluacion_muscular")
    fun deleteEvaluacionMuscular()

    @Query("SELECT * FROM evaluacion_muscular WHERE idConsult == :idConsult")
    fun getEvaluacionMuscular(idConsult: String): LiveData<EvaluacionMuscular>

    @Insert(onConflict = REPLACE)
    fun insertEvaluacionMusculo(evaluacionMusculo: EvaluacionMusculo)

    @Query("DELETE FROM evaluacion_musculo")
    fun deleteEvaluacionMusculo()

    @Query("SELECT * FROM evaluacion_musculo WHERE idEvaluacionMuscular == :idEvaluacionMuscular")
    fun getEvaluacionMusculo(idEvaluacionMuscular: String): LiveData<List<EvaluacionMusculo>>

    @Insert(onConflict = REPLACE)
    fun insertMarcha(marcha: Marcha)

    @Query("DELETE FROM marcha")
    fun deleteMarcha()

    @Query("SELECT * FROM marcha WHERE idConsult == :idConsult")
    fun getMarcha(idConsult: String): LiveData<Marcha>

    @Insert(onConflict = REPLACE)
    fun insertAnalisis(analisis: Analisis)

    @Query("DELETE FROM analisis")
    fun deleteAnalisis()

    @Query("SELECT * FROM analisis WHERE idMarcha == :idMarcha")
    fun getAnalisis(idMarcha: String): LiveData<Analisis>

    @Insert(onConflict = REPLACE)
    fun insertValoracionFuncional(valoracionFuncional: ValoracionFuncional)

    @Query("DELETE FROM valoracion_funcional")
    fun deleteValoracionFuncional()

    @Query("SELECT * FROM valoracion_funcional WHERE idConsult == :idConsult")
    fun getValoracionFuncional(idConsult: String): LiveData<ValoracionFuncional>

    @Insert(onConflict = REPLACE)
    fun insertPlan(plan: Plan)

    @Query("DELETE FROM `plan`")
    fun deletePlan()

    @Query("SELECT * FROM `plan` WHERE idConsult == :idConsult")
    fun getPlan(idConsult: String): LiveData<Plan>

    @Transaction
    @Query("SELECT * FROM consultations WHERE idPatient == :idPatient")
    fun getConsultation(idPatient: String): LiveData<List<ConsultationRelation>>

}