package com.example.exoesqueletov1.data.firebase

import android.util.Log
import com.example.exoesqueletov1.data.firebase.models.ProfileModel.Companion.toProfile
import com.example.exoesqueletov1.data.firebase.models.UserModel
import com.example.exoesqueletov1.data.firebase.models.UserModel.Companion.toUserModel
import com.example.exoesqueletov1.data.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseService @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    fun getProfile(id: String, result: (Resource<UserModel>) -> Unit) {
        result.invoke(Resource.loading())
        val userCollection = db.collection(id)
        userCollection.document(Constants.DOCUMENT_USER)
            .addSnapshotListener { valueUser, errorUser ->
                if (errorUser != null) {
                    result.invoke(Resource.error(errorUser))
                    Log.e(FirebaseService::class.java.name, "Error Snapshot getUser()", errorUser)
                    return@addSnapshotListener
                }
                if (valueUser != null && valueUser.exists()) {
                    val user = valueUser.toUserModel()!!
                    userCollection.document(Constants.DOCUMENT_PROFILE)
                        .addSnapshotListener { valueProfile, errorProfile ->
                            if (errorProfile != null) {
                                result.invoke(Resource.error(errorProfile))
                                Log.e(
                                    FirebaseService::class.java.name,
                                    "Error Snapshot getProfile()",
                                    errorUser
                                )
                                return@addSnapshotListener
                            }
                            if (valueProfile != null && valueProfile.exists()) {
                                user.profile = valueProfile.toProfile()!!
                                result.invoke(Resource.success(user))
                            } else {
                                result.invoke(
                                    Resource.notExist(
                                        user,
                                        Constants.Exist.ProfileDocument
                                    )
                                )
                            }
                        }
                } else {
                    result.invoke(Resource.notExist(Constants.Exist.UserDocument))
                }
            }
    }
}