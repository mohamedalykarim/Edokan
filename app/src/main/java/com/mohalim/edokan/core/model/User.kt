package com.mohalim.edokan.core.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("role") val role: String,
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("points") val points: Int,
    @SerializedName("enabled") val isEnable: Int
)
