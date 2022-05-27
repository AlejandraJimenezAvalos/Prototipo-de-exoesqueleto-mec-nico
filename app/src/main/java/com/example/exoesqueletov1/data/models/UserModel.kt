package com.example.exoesqueletov1.data.models

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.data.firebase.Constants
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class UserModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.EMAIL) val email: String,
    @ColumnInfo(name = Constants.COUNTRY) val country: String,
    @ColumnInfo(name = Constants.DATE) val date: String,
    @ColumnInfo(name = Constants.GENDER) val gender: String,
    @ColumnInfo(name = Constants.LAST_NAME) val lastName: String,
    @ColumnInfo(name = Constants.NAME) val name: String,
    @ColumnInfo(name = Constants.USER) val user: String,
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toUserModel(): UserModel? {
            return try {
                UserModel(
                    getString(Constants.ID       )!!,
                    getString(Constants.EMAIL    )!!,
                    getString(Constants.COUNTRY  )!!,
                    getString(Constants.DATE     )!!,
                    getString(Constants.GENDER   )!!,
                    getString(Constants.LAST_NAME)!!,
                    getString(Constants.NAME     )!!,
                    getString(Constants.USER     )!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_user", "Error to convert user", e)
                null
            }
        }
    }
}
