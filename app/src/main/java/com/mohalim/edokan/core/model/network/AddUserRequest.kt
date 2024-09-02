package com.mohalim.edokan.core.model.network

import com.google.gson.annotations.SerializedName

data class AddUserRequest(
    @SerializedName("username") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number")val phoneNumber: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("city_name") val cityName: String
)