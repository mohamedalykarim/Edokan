package com.mohalim.edokan.core.datasource.network

import androidx.browser.trusted.Token
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApiService {

    @GET("/dev/User/get-user-by-id")
    suspend fun getUserById(@Header("Authorization") token: String) : Response<ResponseBody>


}