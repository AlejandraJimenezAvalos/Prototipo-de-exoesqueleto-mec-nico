package com.example.exoesqueletov1.data.local.query

import androidx.room.ColumnInfo
import com.example.exoesqueletov1.utils.Constants

data class ExoskeletonQuery(
    @ColumnInfo(name = Constants.ID) val id: String,
    @ColumnInfo(name = Constants.MAC) val mac: String,
    @ColumnInfo(name = Constants.USER_ID) val userId: String,
    @ColumnInfo(name = Constants.NAME) val name: String,
    @ColumnInfo(name = Constants.STATUS) var status: String
)
