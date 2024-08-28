package com.mohalim.edokan.core.datasource.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.mohalim.edokan.core.datasource.network.UserApiService
import com.mohalim.edokan.core.model.User
import com.mohalim.edokan.core.model.network.UserResponse
import com.mohalim.edokan.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(val userApiService: UserApiService) {


    fun getUserDataFromDatabase(token: String)= flow<Resource<out User>> {
        emit(Resource.Loading())
        try{
            val response = userApiService.getUserById(token)
            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    val user = userResponse.result
                    emit(Resource.Success(user))
                }
            } else {
                emit(Resource.Error(message = response.body()?.message.toString()))
            }
        }catch (e:Exception){

            emit(Resource.Error(""+e.message))
        }
    }

    fun addNewUser(token: String, userName : String, phoneNumber : String, imageUrl : String, cityId : Int, cityName : String) = flow<Resource<out Boolean>> {
        emit(Resource.Loading())
        try{
            val response = userApiService.addNewUser(token, userName, phoneNumber, imageUrl, cityId, cityName)
            if (response.isSuccessful){
                emit(Resource.Success(true))
            }else{
                emit(Resource.Error("Can't add the user"))
            }
        }catch (e:Exception){
            Log.d("TAG", "getUserDataFromDatabaseById: error "+ e.message)

            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }

    }.flowOn(Dispatchers.IO)

}