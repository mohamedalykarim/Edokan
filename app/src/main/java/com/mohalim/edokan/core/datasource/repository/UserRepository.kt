package com.mohalim.edokan.core.datasource.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mohalim.edokan.core.datasource.network.UserApiService
import com.mohalim.edokan.core.model.User
import com.mohalim.edokan.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.Body
import javax.inject.Inject

class UserRepository @Inject constructor(val userApiService: UserApiService) {


    fun getUserDataFromDatabase(token: String)= flow<Resource<out User>> {
        emit(Resource.Loading())
        try{
            val response = userApiService.getUserById(token)
            if (response.isSuccessful){
                response.body()?.let { responseBody ->
                    // Convert the ResponseBody to a JSON string
                    val jsonString = responseBody.string()

                    Log.d("TAG", "getUserDataFromDatabase: "+ responseBody.string())

                    // Convert JSON string to a JsonObject using Gson
                    val jsonObject: JsonObject = Gson().fromJson(jsonString, JsonObject::class.java)

                    // Now you can work with jsonObject
                    println(jsonObject)
                }
            }else{
                Log.d("TAG", "getUserDataFromDatabaseById: error ++"+ response.message()+ response.body())
            }
        }catch (e:Exception){
            Log.d("TAG", "getUserDataFromDatabaseById: error "+ e.message)

            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
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