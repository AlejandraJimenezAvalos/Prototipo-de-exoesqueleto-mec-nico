package com.example.exoesqueletov1.service

import com.example.exoesqueletov1.BuildConfig

object ConstantsBluetooth {

    // values have to be globally unique
    var INTENT_ACTION_DISCONNECT: String = BuildConfig.APPLICATION_ID + ".Disconnect"
    var NOTIFICATION_CHANNEL: String = BuildConfig.APPLICATION_ID + ".Channel"
    var INTENT_CLASS_MAIN_ACTIVITY: String = BuildConfig.APPLICATION_ID + ".MainActivity"

    // values have to be unique within each app
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001

    enum class QueueType { Connect, ConnectError, Read, IoError }
}