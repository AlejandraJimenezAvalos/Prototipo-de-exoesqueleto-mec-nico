package com.example.exoesqueletov1.data.firebase

import android.util.Log
import com.example.exoesqueletov1.data.models.ExoskeletonModel
import com.example.exoesqueletov1.data.models.ExoskeletonModel.Companion.toExoskeleton
import com.example.exoesqueletov1.data.models.MessageModel
import com.example.exoesqueletov1.data.models.MessageModel.Companion.toMessage
import com.example.exoesqueletov1.data.models.ProfileModel
import com.example.exoesqueletov1.data.models.ProfileModel.Companion.toProfile
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.data.models.UserModel.Companion.toUserModel
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Constants.COLLECTION_EXOSKELETON
import com.example.exoesqueletov1.utils.Constants.COLLECTION_MESSAGES
import com.example.exoesqueletov1.utils.Constants.COLLECTION_PROFILE
import com.example.exoesqueletov1.utils.Constants.COLLECTION_USER
import com.example.exoesqueletov1.utils.Resource
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseService @Inject constructor() {

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

    fun getProfile(id: String, result: (Resource<ProfileModel>) -> Unit) {
        val userProfile = db.collection(COLLECTION_PROFILE).document(id)
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
            .document(profileModel.id).set(profileModel)
            .addOnFailureListener {
                resource.invoke(Resource.error(it))
            }.addOnSuccessListener {
                resource.invoke(Resource.success(it))
            }
    }

    fun getMessages(id: String, result: (Resource<MessageModel>) -> Unit) {
        result.invoke(Resource.loading())
        db.collection(COLLECTION_MESSAGES)
            .whereEqualTo(Constants.FROM, id)
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
            .whereEqualTo(Constants.TO, id)
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

    fun getExoskeleton(id: String, user: String, result: (Resource<ExoskeletonModel>) -> Unit) {
        if (user.getTypeUser() == Constants.TypeUser.Admin) {
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
            db.collection(COLLECTION_EXOSKELETON).whereEqualTo(Constants.USER_ID, id)
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
}