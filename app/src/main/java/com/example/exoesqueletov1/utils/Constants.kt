package com.example.exoesqueletov1.utils

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi

object Constants {

    enum class Status { Success, Failure, Canceled, Loading, NotExist }
    enum class Gender { Woman, Man }
    enum class ActionUsers { Delete, LogIn }
    enum class Connected { False, Pending, True }
    enum class TypeUser { Admin, Specialist, Patient }
    enum class SingInPagerNavigation { Login, SingIn }
    enum class Menu { Home, Profile, Chats, Control, WorkSpecialist, LogOut }
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

}