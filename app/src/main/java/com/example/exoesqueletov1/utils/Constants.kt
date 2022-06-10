package com.example.exoesqueletov1.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object Constants {

    enum class Status { Success, Failure, Canceled, Loading, NotExist }
    enum class Gender { Woman, Man }
    enum class ActionUsers { Delete, LogIn }
    enum class TypeUser { Admin, Specialist, Patient }
    enum class SingInPagerNavigation { Login, SingIn }
    enum class Menu { Home, Profile, Chats, Control, WorkSpecialist, LogOut }

    const val COLLECTION_PROFILE = "profile"
    const val COLLECTION_USER = "user"
    const val COLLECTION_MESSAGES = "messages"
    const val ID = "id"
    const val ADDRESS = "address"
    const val CELL = "cell"
    const val EMAIL = "email"
    const val NAME = "name"
    const val PHONE = "phone"
    const val SCHOOL = "school"
    const val USER = "user"
    const val DESCRIPTION = "description"
    const val IS_VERIFY_BY_ADMIN = "isVerifyByAdmin"
    const val COUNTRY = "country"
    const val DATE = "date"
    const val GENDER = "gender"
    const val LAST_NAME = "lastName"
    const val PASSWORD = "password"
    const val PHOTO_PATH = "photoPath"
    const val ID_USER = "idUser"
    const val FROM = "from"
    const val TO = "to"
    const val MESSAGE = "message"
    const val STATUS = "status"

    @RequiresApi(Build.VERSION_CODES.S)
    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )

}