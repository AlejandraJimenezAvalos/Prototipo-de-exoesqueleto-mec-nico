package com.example.exoesqueletov1.utils

import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.firebase.Constants.Status.*
import java.lang.Exception

data class Resource<out T>(
    val status: Constants.Status,
    val data: T?,
    val exception: Exception?,
    val exist: Constants.Exist?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Success, data, null, null)
        fun <T> error(exception: Exception): Resource<T> = Resource(Failure, null, exception, null)
        fun <T> loading(): Resource<T> = Resource(Loading, null, null, null)
        fun <T> canceled(): Resource<T> = Resource(Canceled, null, null, null)
        fun <T> notExist(exist: Constants.Exist): Resource<T> =
            Resource(NotExist, null, null, exist)
        fun <T> notExist(data: T?, exist: Constants.Exist): Resource<T> =
            Resource(NotExist, data, null, exist)
    }
}
