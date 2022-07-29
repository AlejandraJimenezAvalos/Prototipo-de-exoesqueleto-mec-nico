package com.example.exoesqueletov1.data.firebase

import android.util.Log
import com.example.exoesqueletov1.data.local.relations.ConsultationRelation
import com.example.exoesqueletov1.data.models.*
import com.example.exoesqueletov1.data.models.ExoskeletonModel.Companion.toExoskeleton
import com.example.exoesqueletov1.data.models.ExpedientModel.Companion.toExpedientModel
import com.example.exoesqueletov1.data.models.MessageModel.Companion.toMessage
import com.example.exoesqueletov1.data.models.PatientModel.Companion.toPatientModel
import com.example.exoesqueletov1.data.models.ProfileModel.Companion.toProfile
import com.example.exoesqueletov1.data.models.UserModel.Companion.toUserModel
import com.example.exoesqueletov1.data.models.consultation.*
import com.example.exoesqueletov1.data.models.consultation.Analisis.Companion.getAnalisis
import com.example.exoesqueletov1.data.models.consultation.ConsultationData.Companion.toConsultationData
import com.example.exoesqueletov1.data.models.consultation.Diagnostico.Companion.getDiagnostico
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMuscular.Companion.getEvaluacionMuscular
import com.example.exoesqueletov1.data.models.consultation.EvaluacionMusculo.Companion.getEvaluacionMusculo
import com.example.exoesqueletov1.data.models.consultation.EvaluacionPostura.Companion.getEvaluacionPostura
import com.example.exoesqueletov1.data.models.consultation.ExploracionFisica.Companion.toExploracionFisica
import com.example.exoesqueletov1.data.models.consultation.Marcha.Companion.getMarcha
import com.example.exoesqueletov1.data.models.consultation.Plan.Companion.getPlan
import com.example.exoesqueletov1.data.models.consultation.ValoracionFuncional.Companion.getValoracionFuncional
import com.example.exoesqueletov1.domain.UserRepository
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Constants.COLLECTION_ANALISIS
import com.example.exoesqueletov1.utils.Constants.COLLECTION_CONSULTATION_DATA
import com.example.exoesqueletov1.utils.Constants.COLLECTION_DIAGNOSTICO
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EVALUACION_MUSCULAR
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EVALUACION_MUSCULO
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EVALUACION_POSTURA
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EXOSKELETON
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EXPEDIENT
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EXPLORACION_FISICA
import com.example.exoesqueletov1.utils.Constants.COLLECTION_MARCHA
import com.example.exoesqueletov1.utils.Constants.COLLECTION_MESSAGES
import com.example.exoesqueletov1.utils.Constants.COLLECTION_PATIENT
import com.example.exoesqueletov1.utils.Constants.COLLECTION_PLAN
import com.example.exoesqueletov1.utils.Constants.COLLECTION_PROFILE
import com.example.exoesqueletov1.utils.Constants.COLLECTION_USER
import com.example.exoesqueletov1.utils.Constants.COLLECTION_VALORACION_FUNCIONAL
import com.example.exoesqueletov1.utils.Constants.ID_CONSULT
import com.example.exoesqueletov1.utils.Constants.ID_EVALUACION_MUSCULAR
import com.example.exoesqueletov1.utils.Constants.ID_MARCHA
import com.example.exoesqueletov1.utils.Constants.ID_USER
import com.example.exoesqueletov1.utils.Resource
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseService @Inject constructor(private val userRepository: UserRepository) {

    private val db = Firebase.firestore

    fun getUser(id: String, result: (Resource<UserModel>) -> Unit) {
        val userCollection = db.collection(COLLECTION_USER).document(id)
        result.invoke(Resource.loading())
        userCollection.addSnapshotListener { valueUser, errorUser ->
            if (errorUser != null) {
                result.invoke(Resource.error(errorUser))
                Log.e(FirebaseService::class.java.name, "Error Snapshot getUser()", errorUser)
                return@addSnapshotListener
            }
            if (valueUser != null && valueUser.exists()) {
                val user = valueUser.toUserModel()!!
                result.invoke(Resource.success(user))
                if (user.user.getTypeUser() == Constants.TypeUser.Admin) {
                    db.collection(COLLECTION_USER).addSnapshotListener { valueUsers, errorUsers ->
                        if (errorUsers != null) {
                            result.invoke(Resource.error(errorUsers))
                            Log.e(
                                FirebaseService::class.java.name,
                                "Error Snapshot getProfile()",
                                errorUsers
                            )
                        }
                        if (valueUsers != null && !valueUsers.isEmpty) {
                            for (document in valueUsers) {
                                if (document.exists() && (document.toUserModel()!!.id != id))
                                    result.invoke(Resource.success(document.toUserModel()!!))
                            }
                        } else result.invoke(Resource.notExist())
                    }
                }
            } else {
                result.invoke(Resource.notExist())
            }
        }
    }

    fun setUser(user: UserModel, resource: (Resource<Void>) -> Unit) {
        resource.invoke(Resource.loading())
        db.collection(COLLECTION_USER).document(user.id).set(user)
            .addOnFailureListener {
                resource.invoke(Resource.error(it))
            }.addOnSuccessListener {
                resource.invoke(Resource.success(it))
            }
    }

    fun getProfile(result: (Resource<ProfileModel>) -> Unit) {
        val userProfile = db.collection(COLLECTION_PROFILE).document(userRepository.getId())
        result.invoke(Resource.loading())
        userProfile.addSnapshotListener { value, error ->
            if (error != null) {
                result.invoke(Resource.error(error))
                Log.e(FirebaseService::class.java.name, "Error Snapshot getProfile()", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val user = value.toProfile()!!
                result.invoke(Resource.success(user))
            } else result.invoke(Resource.notExist())
        }
    }

    fun setProfile(profileModel: ProfileModel, resource: (Resource<Void>) -> Unit) {
        resource.invoke(Resource.loading())
        db.collection(COLLECTION_PROFILE)
            .document(userRepository.getId()).set(profileModel)
            .addOnFailureListener {
                resource.invoke(Resource.error(it))
            }.addOnSuccessListener {
                resource.invoke(Resource.success(it))
            }
    }

    fun getMessages(result: (Resource<MessageModel>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_MESSAGES)
            .whereEqualTo(Constants.FROM, userRepository.getId())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(FirebaseService::class.java.name, "Error Snapshot getProfile()", error)
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) result.invoke(Resource.success(document.toMessage()!!))
                    }
                } else result.invoke(Resource.notExist())
            }

        db.collection(COLLECTION_MESSAGES)
            .whereEqualTo(Constants.TO, userRepository.getId())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(FirebaseService::class.java.name, "Error Snapshot getProfile()", error)
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) result.invoke(Resource.success(document.toMessage()!!))
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun setMessage(messageModel: MessageModel, result: (Resource<Void>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_MESSAGES)
            .document(messageModel.id)
            .set(messageModel)
            .addOnSuccessListener {
                result.invoke(Resource.success(it))
            }.addOnFailureListener {
                result.invoke(Resource.error(it))
            }

    }

    fun getExoskeleton(result: (Resource<ExoskeletonModel>) -> Unit) {
        if (userRepository.getType().getTypeUser() == Constants.TypeUser.Admin) {
            db.collection(COLLECTION_EXOSKELETON).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getExoskeleton()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) result.invoke(Resource.success(document.toExoskeleton()!!))
                    }
                } else result.invoke(Resource.notExist())
            }
        } else {
            db.collection(COLLECTION_EXOSKELETON)
                .whereEqualTo(Constants.USER_ID, userRepository.getId())
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        result.invoke(Resource.error(error))
                        Log.e(
                            FirebaseService::class.java.name,
                            "Error Snapshot getExoskeleton()",
                            error
                        )
                        return@addSnapshotListener
                    }
                    if (value != null && !value.isEmpty) {
                        for (document in value) {
                            if (document.exists()) result.invoke(Resource.success(document.toExoskeleton()!!))
                        }
                    } else result.invoke(Resource.notExist())
                }
        }
    }

    fun setExoskeleton(exoskeletonModel: ExoskeletonModel, result: (Resource<Void>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_EXOSKELETON).document(exoskeletonModel.id)
            .set(exoskeletonModel).addOnSuccessListener {
                result.invoke(Resource.success(it))
            }.addOnFailureListener {
                result.invoke(Resource.error(it))
            }
    }

    fun getPatient(result: (Resource<PatientModel>) -> Unit) {
        when (userRepository.getType().getTypeUser()) {
            Constants.TypeUser.Specialist -> {
                result.invoke(Resource.loading())
                db.collection(COLLECTION_PATIENT).whereEqualTo(ID_USER, userRepository.getId())
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            result.invoke(Resource.error(error))
                            Log.e(
                                FirebaseService::class.java.name,
                                "Error Snapshot getPatient()",
                                error
                            )
                            return@addSnapshotListener
                        }
                        if (value != null && !value.isEmpty) {
                            for (document in value) {
                                if (document.exists())
                                    result.invoke(Resource.success(document.toPatientModel()!!))
                            }
                        } else result.invoke(Resource.notExist())
                    }
            }
            Constants.TypeUser.Admin -> {
                db.collection(COLLECTION_PATIENT).addSnapshotListener { value, error ->
                    if (error != null) {
                        result.invoke(Resource.error(error))
                        Log.e(
                            FirebaseService::class.java.name,
                            "Error Snapshot getPatient()",
                            error
                        )
                        return@addSnapshotListener
                    }
                    if (value != null && !value.isEmpty) {
                        for (document in value) {
                            if (document.exists())
                                result.invoke(Resource.success(document.toPatientModel()!!))
                        }
                    } else result.invoke(Resource.notExist())
                }
            }
            else -> {}
        }

    }

    fun setPatient(patientModel: PatientModel, result: (Resource<Void>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_PATIENT).document(patientModel.id)
            .set(patientModel).addOnSuccessListener {
                result.invoke(Resource.success(it))
            }.addOnFailureListener {
                result.invoke(Resource.error(it))
            }
    }

    fun setExpedient(expedientModel: ExpedientModel, result: (Resource<Void>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_EXPEDIENT).document(expedientModel.id)
            .set(expedientModel).addOnSuccessListener {
                result.invoke(Resource.success(it))
            }.addOnFailureListener {
                result.invoke(Resource.error(it))
            }
    }

    fun getExpedient(result: (Resource<ExpedientModel>) -> Unit) {
        when (userRepository.getType().getTypeUser()) {
            Constants.TypeUser.Specialist -> {
                result.invoke(Resource.loading())
                db.collection(COLLECTION_EXPEDIENT).whereEqualTo(ID_USER, userRepository.getId())
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            result.invoke(Resource.error(error))
                            Log.e(
                                FirebaseService::class.java.name,
                                "Error Snapshot getPatient()",
                                error
                            )
                            return@addSnapshotListener
                        }
                        if (value != null && !value.isEmpty) {
                            for (document in value) {
                                if (document.exists())
                                    result.invoke(Resource.success(document.toExpedientModel()!!))
                            }
                        } else result.invoke(Resource.notExist())
                    }
            }
            Constants.TypeUser.Admin -> {
                db.collection(COLLECTION_EXPEDIENT).addSnapshotListener { value, error ->
                    if (error != null) {
                        result.invoke(Resource.error(error))
                        Log.e(
                            FirebaseService::class.java.name,
                            "Error Snapshot getPatient()",
                            error
                        )
                        return@addSnapshotListener
                    }
                    if (value != null && !value.isEmpty) {
                        for (document in value) {
                            if (document.exists())
                                result.invoke(Resource.success(document.toExpedientModel()!!))
                        }
                    } else result.invoke(Resource.notExist())
                }
            }
            else -> {}
        }

    }

    fun setConsultation(list: List<ConsultationRelation>, function: (Resource<Void>) -> Unit) {
        val rfConsultation = db.collection(COLLECTION_CONSULTATION_DATA)
        val rfMarcha = db.collection(COLLECTION_MARCHA)
        val rfMuscular = db.collection(COLLECTION_EVALUACION_MUSCULAR)
        val rfPostura = db.collection(COLLECTION_EVALUACION_POSTURA)
        val rfExploracion = db.collection(COLLECTION_EXPLORACION_FISICA)
        val rfDiagnostico = db.collection(COLLECTION_DIAGNOSTICO)
        val rfValoracion = db.collection(COLLECTION_VALORACION_FUNCIONAL)
        val rfPlan = db.collection(COLLECTION_PLAN)
        val rfMusculo = db.collection(COLLECTION_EVALUACION_MUSCULO)
        val rfAnalisis = db.collection(COLLECTION_ANALISIS)
        list.forEach { consultation ->
            rfConsultation.document(consultation.consultation!!.id).set(consultation.consultation)
                .addOnFailureListener {
                    function.invoke(Resource.error(it))
                }
            if (consultation.marcha != null && consultation.marcha.marcha != null)
                rfMarcha.document(consultation.marcha.marcha.id)
                    .set(consultation.marcha.marcha)
                    .addOnFailureListener {
                        function.invoke(Resource.error(it))
                    }
            if (consultation.evaluacionMuscular != null && consultation.evaluacionMuscular.evaluacionMuscular != null)
                rfMuscular.document(consultation.evaluacionMuscular.evaluacionMuscular.id)
                    .set(consultation.evaluacionMuscular.evaluacionMuscular)
                    .addOnFailureListener {
                        function.invoke(Resource.error(it))
                    }
            if (consultation.evaluacionPostura != null)
                consultation.evaluacionPostura.forEach { evaluacionPostura ->
                    rfPostura.document(evaluacionPostura.id).set(evaluacionPostura)
                        .addOnFailureListener {
                            function.invoke(Resource.error(it))
                        }
                }
            if (consultation.exploracionFisica != null)
                rfExploracion.document(consultation.exploracionFisica.id)
                    .set(consultation.exploracionFisica)
                    .addOnFailureListener {
                        function.invoke(Resource.error(it))
                    }
            if (consultation.diagnostico != null)
                rfDiagnostico.document(consultation.diagnostico.id)
                    .set(consultation.diagnostico)
                    .addOnFailureListener {
                        function.invoke(Resource.error(it))
                    }
            if (consultation.valoracionFuncional != null)
                rfValoracion.document(consultation.valoracionFuncional.id)
                    .set(consultation.valoracionFuncional)
                    .addOnFailureListener {
                        function.invoke(Resource.error(it))
                    }
            if (consultation.plan != null) rfPlan.document(consultation.plan.id)
                .set(consultation.plan).addOnFailureListener {
                    function.invoke(Resource.error(it))
                }
            if (consultation.evaluacionMuscular != null && consultation.evaluacionMuscular.evaluacionMusculo != null)
                consultation.evaluacionMuscular.evaluacionMusculo!!.forEach { evaluacionMusculo ->
                    rfMusculo.document(evaluacionMusculo.id).set(evaluacionMusculo)
                        .addOnFailureListener {
                            function.invoke(Resource.error(it))
                        }
                }
            if (consultation.marcha != null && consultation.marcha.analisis != null)
                consultation.marcha.analisis.forEach { analisis ->
                    rfAnalisis.document(analisis.id).set(analisis).addOnFailureListener {
                        function.invoke(Resource.error(it))
                    }
                }
        }
    }

    fun getConsultation(result: (Resource<ConsultationData>) -> Unit) {
        result.invoke(Resource.loading())

        db.collection(COLLECTION_CONSULTATION_DATA)
            .whereEqualTo(ID_USER, userRepository.getId()).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getConsultation()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.toConsultationData()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getMarcha(id: String, result: (Resource<Marcha>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_MARCHA)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getMarcha()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getMarcha()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getEvaluacionMuscular(id: String, result: (Resource<EvaluacionMuscular>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_EVALUACION_MUSCULAR)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getEvaluacionMuscular()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getEvaluacionMuscular()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getEvaluacionPostura(id: String, result: (Resource<EvaluacionPostura>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_EVALUACION_POSTURA)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getEvaluacionPostura()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getEvaluacionPostura()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getExploracionFisica(id: String, result: (Resource<ExploracionFisica>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_EXPLORACION_FISICA)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getEvaluacionPostura()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.toExploracionFisica()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getDiagnostico(id: String, result: (Resource<Diagnostico>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_DIAGNOSTICO)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getDiagnostico()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getDiagnostico()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getValoracionFuncional(id: String, result: (Resource<ValoracionFuncional>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_VALORACION_FUNCIONAL)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getValoracionFuncional()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getValoracionFuncional()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getPlan(id: String, result: (Resource<Plan>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_PLAN)
            .whereEqualTo(ID_CONSULT, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getPlan()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getPlan()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getEvaluacionMusculo(id: String, result: (Resource<EvaluacionMusculo>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_EVALUACION_MUSCULO)
            .whereEqualTo(ID_EVALUACION_MUSCULAR, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getPlan()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getEvaluacionMusculo()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

    fun getAnalisis(id: String, result: (Resource<Analisis>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_ANALISIS)
            .whereEqualTo(ID_MARCHA, id).addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(Resource.error(error))
                    Log.e(
                        FirebaseService::class.java.name,
                        "Error Snapshot getPlan()",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) {
                    for (document in value) {
                        if (document.exists()) {
                            result.invoke(Resource.success(document.getAnalisis()))
                        }
                    }
                } else result.invoke(Resource.notExist())
            }
    }

}