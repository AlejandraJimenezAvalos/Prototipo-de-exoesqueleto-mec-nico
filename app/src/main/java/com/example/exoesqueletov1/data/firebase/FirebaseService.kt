package com.example.exoesqueletov1.data.firebase

import android.util.Log
import com.example.exoesqueletov1.data.models.UserModel
import com.example.exoesqueletov1.data.models.UserModel.Companion.toUserModel
import com.example.exoesqueletov1.utils.Resource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseService @Inject constructor() {

    private val db = Firebase.firestore

    fun getUser(id: String, result: (Resource<UserModel>) -> Unit) {
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
                    result.invoke(Resource.success(user))
                } else {
                    result.invoke(Resource.notExist(Constants.Exist.UserDocument))
                }
            }
    }

    fun setUser(user: UserModel, resource: (Resource<Void>) -> Unit) {
        resource.invoke(Resource.loading())
        db.collection(user.id).document(Constants.DOCUMENT_USER).set(user)
            .addOnFailureListener {
                resource.invoke(Resource.error(it))
            }.addOnSuccessListener {
                resource.invoke(Resource.success(it))
            }
    }
}