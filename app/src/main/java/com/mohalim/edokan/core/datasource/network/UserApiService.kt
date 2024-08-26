package com.mohalim.edokan.core.datasource.network

import androidx.browser.trusted.Token
import com.mohalim.edokan.core.model.network.UserResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {

    @GET("/Development/User/get-user-by-id")
    suspend fun getUserById(@Header("Authorization") token: String): Response<UserResponse>

    @POST("/Development/User/add-new-user")
    suspend fun addNewUser(
        @Header("Authorization") token: String,
        @Body userName : String,
        @Body phoneNumber : String,
        @Body imageUrl : String,
        @Body cityId : Int,
        @Body cityName : String
        ): Response<ResponseBody>
}