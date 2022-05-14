package com.example.exoesqueletov1.data.firebase.models

import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import com.example.exoesqueletov1.data.firebase.Constants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class UserModel(
    val id: String,
    val email: String,
    val country: String,
    val date: String,
    val gender: String,
    val lastName: String,
    val name: String,
    val user: String,
) : Parcelable {
    @IgnoredOnParcel
    lateinit var profile: ProfileModel

    companion object {
        fun DocumentSnapshot.toUserModel(): UserModel? {
            return try {
                UserModel(
                    id,
                    getString(Constants.EMAIL)!!,
                    getString(Constants.COUNTRY)!!,
                    getString(Constants.DATE)!!,
                    getString(Constants.GENDER)!!,
                    getString(Constants.LAST_NAME)!!,
                    getString(Constants.NAME)!!,
                    getString(Constants.USER)!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_user", "Error to convert user", e)
                null
            }
        }

        fun FirebaseUser.toUser(): UserModel = UserModel(
            uid,
            email!!,
            "",
            "",
            "",
            "",
            "",
            ""
        )
    }

}
