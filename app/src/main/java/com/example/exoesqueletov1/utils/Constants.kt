package com.example.exoesqueletov1.utils

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.exoesqueletov1.BuildConfig

object Constants {

    enum class Status { Success, Failure, Canceled, Loading, NotExist }
    enum class Gender { Woman, Man }
    enum class ActionUsers { Delete, LogIn }
    enum class TypeUser { Admin, Specialist, Patient }
    enum class SingInPagerNavigation { Login, SingIn }
    enum class StatusDevice { Emparejado, Cercano }

    const val COLLECTION_PROFILE = "profile"
    const val COLLECTION_USER = "user"
    const val COLLECTION_MESSAGES = "messages"
    const val COLLECTION_EXOSKELETON = "exoskeleton"
    const val ID = "id"
    const val ADDRESS = "address"
    const val CELL = "cell"
    const val EMAIL = "email"
    const val NAME = "name"
    const val PHONE = "phone"
    const val SCHOOL = "school"
    const val USER = "user"
    const val DESCRIPTION = "description"
    const val MAC = "mac"
    const val IS_VERIFY_BY_ADMIN = "isVerifyByAdmin"
    const val COUNTRY = "country"
    const val DATE = "date"
    const val GENDER = "gender"
    const val LAST_NAME = "lastName"
    const val PASSWORD = "password"
    const val PHOTO_PATH = "photoPath"
    const val ID_USER = "idUser"
    const val USER_ID = "userId"
    const val FROM = "from"
    const val TO = "to"
    const val MESSAGE = "message"
    const val STATUS = "status"

    @RequiresApi(Build.VERSION_CODES.S)
    val PERMISSIONS = arrayOf(
        INTERNET,
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE,
        CALL_PHONE,
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION,
        CAMERA,
        FOREGROUND_SERVICE,
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        BLUETOOTH_SCAN,
        BLUETOOTH_ADVERTISE,
        BLUETOOTH_CONNECT,
    )

    // values have to be globally unique
    const val INTENT_ACTION_DISCONNECT: String = BuildConfig.APPLICATION_ID + ".Disconnect"
    const val NOTIFICATION_CHANNEL: String = BuildConfig.APPLICATION_ID + ".Channel"
    const val INTENT_CLASS_MAIN_ACTIVITY: String = BuildConfig.APPLICATION_ID + ".MainActivity"

    // values have to be unique within each app
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001

    enum class StatusConnection { Connect, ConnectError, Read, IoError }
    enum class Connection { False, Pending, True }
    enum class StatusBluetoothDevice { Pending, Connected, Error, Read, Disconnected, ConnectionFailed }

}