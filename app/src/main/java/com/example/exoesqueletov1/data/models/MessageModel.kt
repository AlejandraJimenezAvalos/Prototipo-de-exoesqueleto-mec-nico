package com.example.exoesqueletov1.data.models

import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exoesqueletov1.utils.Constants
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Entity(tableName = "messages")
@Parcelize
data class MessageModel(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = Constants.DATE) val date: String,
    @ColumnInfo(name = Constants.FROM) val from: String,
    @ColumnInfo(name = Constants.TO) val to: String,
    @ColumnInfo(name = Constants.MESSAGE) val message: String,
    @ColumnInfo(name = Constants.STATUS) val status: Boolean,
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toMessage(): MessageModel? {
            return try {
                MessageModel(
                    id,
                    getDate(Constants.DATE)!!.toString(),
                    getString(Constants.FROM)!!,// eEoLKvDFNDYiJbEilP8DZ8hX6b83
                    getString(Constants.TO)!!,
                    getString(Constants.MESSAGE)!!,
                    getBoolean(Constants.STATUS)!!,
                )
            } catch (e: Exception) {
                Log.e("Error_to_message", "Error to convert message", e)
                null
            }
        }
    }
}