package com.example.exoesqueletov1.data.local.query

import androidx.room.ColumnInfo
import com.example.exoesqueletov1.ui.activity.main.MainActivity
import com.example.exoesqueletov1.ui.fragments.pairedDevises.adapter.PairedDevicesViewHolder
import com.example.exoesqueletov1.utils.Constants
import com.google.common.eventbus.EventBus

/**
 * This class used by [EventBus] in [PairedDevicesViewHolder.bind] to [MainActivity.getDevice]
 * by connect with Bluetooth device.
 */
data class ExoskeletonQuery(
    @ColumnInfo(name = Constants.ID) val id: String,
    @ColumnInfo(name = Constants.MAC) val mac: String,
    @ColumnInfo(name = Constants.USER_ID) val userId: String,
    @ColumnInfo(name = Constants.NAME) var name: String,
    @ColumnInfo(name = Constants.STATUS) var status: String
)
