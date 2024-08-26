package com.mohalim.edokan.core.model.network

import com.google.gson.annotations.SerializedName
import com.mohalim.edokan.core.model.User

data class UserResponse(
    var message: String,
    var result: User
)
