package com.example.exoesqueletov1.data.models

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "exoskeleton")
@Parcelize
data class ExoskeletonModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.MAC) val mac: String,
    @ColumnInfo(name = Constants.USER_ID) val userId: String,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toExoskeleton(): ExoskeletonModel? {
            return try {
                ExoskeletonModel(
                    id,
                    getString(Constants.MAC)!!,
                    getString(Constants.USER_ID)!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_exoskeleton", "Error to convert exoskeleton", e)
                null
            }
        }
    }
}
