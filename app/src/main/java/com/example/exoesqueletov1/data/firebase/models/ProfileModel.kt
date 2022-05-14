package com.example.exoesqueletov1.data.firebase.models

import android.os.Parcelable
import android.util.Log
import com.example.exoesqueletov1.data.firebase.Constants
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileModel(
    val id: String,
    val address: String,
    val cell: String,
    val email: String,
    val name: String,
    val phone: String,
    val school: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toProfile(): ProfileModel? {
            return try {
                ProfileModel(
                    id,
                    getString(Constants.ADDRESS)!!,
                    getString(Constants.CELL   )!!,
                    getString(Constants.EMAIL  )!!,
                    getString(Constants.NAME   )!!,
                    getString(Constants.PHONE  )!!,
                    getString(Constants.SCHOOL )!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_profile", "Error to convert profile", e)
                null
            }
        }
    }
}