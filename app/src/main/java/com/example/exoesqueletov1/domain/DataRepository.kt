package com.example.exoesqueletov1.domain

import androidx.lifecycle.LiveData
import com.example.exoesqueletov1.data.local.dao.Dao
import com.example.exoesqueletov1.data.local.entity.ChatsEntity
import com.example.exoesqueletov1.data.local.entity.UsersEntity
import com.example.exoesqueletov1.data.models.*
import com.example.exoesqueletov1.data.models.consultation.*
import com.example.exoesqueletov1.data.models.rutina.RutinaModel
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dao: Dao,
    private val patientRepository: PatientRepository,
    private val userRepository: UserRepository,
) {
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
        dao.deleteConsultations()
        dao.deleteExploracionFisica()
        dao.deleteEvaluacionPostura()
        dao.deleteDiagnostico()
        dao.deleteEvaluacionMuscular()
        dao.deleteEvaluacionMusculo()
        dao.deleteMarcha()
        dao.deleteAnalisis()
        dao.deleteValoracionFuncional()
        dao.deletePlan()
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
    fun getExpedients(id: String) = dao.getExpedients(id)

    fun setConsultation(consultation: Consultation) {
        consultation.consultationData.idPatient = patientRepository.getId()
        dao.insertConsultation(consultation.consultationData)
        dao.insertExploracionFisica(consultation.exploracionFisica)
        consultation.listEvaluacionPostura.forEach {
            dao.insertEvaluacionPostura(it)
        }
        dao.insertDiagnostico(consultation.diagnostico)
        dao.insertEvaluacionMuscular(consultation.evaluacionMuscular)
        consultation.listEvaluacionMusculo.forEach {
            dao.insertEvaluacionMusculo(it)
        }
        dao.insertMarcha(consultation.marcha)
        consultation.listAnalisis.forEach {
            dao.insertAnalisis(it)
        }
        dao.insertValoracionFuncional(consultation.valoracionFuncional)
        dao.insertPlan(consultation.plan)
    }

    fun getConsultations() = dao.getConsultationsComplete()

    fun insertConsultation(consultationData: ConsultationData) =
        dao.insertConsultation(consultationData)

    fun insertExploracionFisica(exploracionFisica: ExploracionFisica) =
        dao.insertExploracionFisica(exploracionFisica)

    fun insertEvaluacionPostura(evaluacionPostura: EvaluacionPostura) =
        dao.insertEvaluacionPostura(evaluacionPostura)

    fun insertDiagnostico(diagnostico: Diagnostico) = dao.insertDiagnostico(diagnostico)
    fun insertEvaluacionMuscular(evaluacionMuscular: EvaluacionMuscular) =
        dao.insertEvaluacionMuscular(evaluacionMuscular)

    fun insertEvaluacionMusculo(evaluacionMusculo: EvaluacionMusculo) =
        dao.insertEvaluacionMusculo(evaluacionMusculo)

    fun insertMarcha(marcha: Marcha) = dao.insertMarcha(marcha)
    fun insertValoracionFuncional(valoracionFuncional: ValoracionFuncional) =
        dao.insertValoracionFuncional(valoracionFuncional)

    fun insertPlan(plan: Plan) = dao.insertPlan(plan)
    fun insertAnalisis(analisis: Analisis) = dao.insertAnalisis(analisis)

    fun getConsultationsByIdPatient() = dao.getConsultations(patientRepository.getId())

    fun getConsultationComplete() =
        dao.getConsultationComplete(patientRepository.getConsultationId())

    fun insertRutina(rutinaModel: RutinaModel) {
        rutinaModel.userId = userRepository.getId()
        rutinaModel.idPatient = patientRepository.getId()
        dao.insertRutina(rutinaModel)
    }

    fun getRutinas() = dao.getRutinasByPatient(patientRepository.getId())
}