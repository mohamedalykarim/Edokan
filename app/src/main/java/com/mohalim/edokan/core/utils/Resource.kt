package com.mohalim.edokan.core.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading: Resource<Nothing>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}