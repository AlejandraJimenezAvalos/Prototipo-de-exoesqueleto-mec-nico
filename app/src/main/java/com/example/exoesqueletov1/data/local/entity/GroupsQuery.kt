package com.example.exoesqueletov1.data.local.entity

import androidx.room.ColumnInfo
import com.example.exoesqueletov1.utils.Constants

data class GroupsQuery(
    @ColumnInfo(name = Constants.FROM) val from: String,
    @ColumnInfo(name = Constants.TO) val to: String,
)
