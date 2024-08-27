package com.mohalim.edokan.core.model.network

import com.mohalim.edokan.core.model.User

data class UserResponse(
    val statusCode: Int,
    val body: UserBody,
    val headers: Headers
)

data class UserBody(
    val message: String,
    val result: User
)



