package com.mohalim.edokan.core.model

data class User(
    val userId:String = "",
    val username:String = "",
    val email:String = "",
    val phoneNumber:String = "",
    val imageUrl:String = "",
    val role:String = "",
    val cityId:Int = 0,
    val cityName: String = "",
    val points:Int = 0,
    val isEnable:Boolean = true
)
