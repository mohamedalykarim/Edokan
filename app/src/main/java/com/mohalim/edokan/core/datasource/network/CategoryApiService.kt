package com.mohalim.edokan.core.datasource.network

import com.mohalim.edokan.core.model.network.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApiService {

    @GET("/categories/{search_string}")
    suspend fun getCategoriesByName(
        @Header("Authorization") token: String,
        @Path("search_string") searchString: String): Response<CategoriesResponse>
}