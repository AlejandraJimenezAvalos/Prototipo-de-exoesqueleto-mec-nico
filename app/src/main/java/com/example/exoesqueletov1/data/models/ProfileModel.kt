package com.example.exoesqueletov1.data.models

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.data.firebase.Constants
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "profile")
@Parcelize
data class ProfileModel(
    @PrimaryKey(autoGenerate = false        ) val id: String,
    @ColumnInfo(name = Constants.ADDRESS    ) val address: String,
    @ColumnInfo(name = Constants.CELL       ) val cell: String,
    @ColumnInfo(name = Constants.EMAIL      ) val email: String,
    @ColumnInfo(name = Constants.NAME       ) val name: String,
    @ColumnInfo(name = Constants.PHONE      ) val phone: String,
    @ColumnInfo(name = Constants.SCHOOL     ) val school: String,
    @ColumnInfo(name = Constants.DESCRIPTION) val description: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toProfile(): ProfileModel? {
            return try {
                ProfileModel(
                    getString(Constants.ID         )!!,
                    getString(Constants.ADDRESS    )!!,
                    getString(Constants.CELL       )!!,
                    getString(Constants.EMAIL      )!!,
                    getString(Constants.NAME       )!!,
                    getString(Constants.PHONE      )!!,
                    getString(Constants.SCHOOL     )!!,
                    getString(Constants.DESCRIPTION)!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_profile", "Error to convert profile", e)
                null
            }
        }
    }
}