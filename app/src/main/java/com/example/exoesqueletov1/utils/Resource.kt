package com.example.exoesqueletov1.utils

import com.example.exoesqueletov1.data.firebase.Constants
import com.example.exoesqueletov1.data.firebase.Constants.Status.*

data class Resource<out T>(
    val status: Constants.Status,
    val data: T?,
    val exception: Exception?,
) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(Success, data, null)
        fun <T> error(exception: Exception): Resource<T> = Resource(Failure, null, exception)
        fun <T> loading(): Resource<T> = Resource(Loading, null, null)
        fun <T> canceled(): Resource<T> = Resource(Canceled, null, null)
        fun <T> notExist(): Resource<T> = Resource(NotExist, null, null)
    }
}
