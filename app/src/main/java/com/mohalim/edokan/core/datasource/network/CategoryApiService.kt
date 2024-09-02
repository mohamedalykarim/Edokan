package com.mohalim.edokan.core.datasource.network

import com.mohalim.edokan.core.model.network.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CategoryApiService {

    @GET("/Development/Category/get-main-categories")
    suspend fun getMainCategories(
        @Header("Authorization") token: String
    ): Response<CategoriesResponse>

    @GET("/Development/Category/get-categories-by-name/{search_string}")
    suspend fun getCategoriesByName(
        @Header("Authorization") token: String,
        @Query("search_string") searchString: String): Response<CategoriesResponse>
}