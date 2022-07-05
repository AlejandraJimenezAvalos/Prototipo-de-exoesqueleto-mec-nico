package com.example.exoesqueletov1.data.firebase

import android.util.Log
import com.example.exoesqueletov1.data.models.*
import com.example.exoesqueletov1.data.models.ExoskeletonModel.Companion.toExoskeleton
import com.example.exoesqueletov1.data.models.ExpedientModel.Companion.toExpedientModel
import com.example.exoesqueletov1.data.models.MessageModel.Companion.toMessage
import com.example.exoesqueletov1.data.models.PatientModel.Companion.toPatientModel
import com.example.exoesqueletov1.data.models.ProfileModel.Companion.toProfile
import com.example.exoesqueletov1.data.models.UserModel.Companion.toUserModel
import com.example.exoesqueletov1.domain.UserRepository
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EXOSKELETON
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EXPEDIENT
import com.example.exoesqueletov1.utils.Constants.COLLECTION_MESSAGES
import com.example.exoesqueletov1.utils.Constants.COLLECTION_PATIENT
import com.example.exoesqueletov1.utils.Constants.COLLECTION_PROFILE
import com.example.exoesqueletov1.utils.Constants.COLLECTION_USER
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
        db.collection(Constants.COLLECTION_EXPEDIENT).document(expedientModel.id)
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

}