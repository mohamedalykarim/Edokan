package com.mohalim.edokan.core.datasource.repository

import android.util.Log
import com.mohalim.edokan.core.datasource.network.UserApiService
import com.mohalim.edokan.core.model.User
import com.mohalim.edokan.core.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(val userApiService: UserApiService) {


    fun getUserDataFromDatabaseById(token: String, userId: String)= flow<Resource<out User>> {
        emit(Resource.Loading())
        try{
            Log.d("TAG", "getUserDataFromDatabaseById: "+token+" "+userId)
            val response = userApiService.getUserById(token)
            if (response.isSuccessful){
                Log.d("TAG", "getUserDataFromDatabaseById: isSuccessful "+response.body())
            }else{
                Log.d("TAG", "getUserDataFromDatabaseById: error ++"+ response.message()+ response.body())
            }
        }catch (e:Exception){
            Log.d("TAG", "getUserDataFromDatabaseById: error "+ e.message)

            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

}