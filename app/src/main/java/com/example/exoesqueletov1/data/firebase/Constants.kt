package com.example.exoesqueletov1.data.firebase

object Constants {

    enum class Status { Success, Failure, Canceled, Loading, NotExist }
    enum class Gender { Woman, Man }
    enum class ActionUsers { Delete, LogIn }
    enum class TypeUser { Admin, Specialist, Patient }
    enum class SingInPagerNavigation { Login, SingIn }
    enum class Menu { Notification, Profile, Chats, Control, WorkSpecialist, }

    const val DOCUMENT_PROFILE = "profile"
    const val DOCUMENT_USER = "user"
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

}