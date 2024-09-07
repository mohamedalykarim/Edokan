package com.mohalim.edokan.core.datasource.network

import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.network.AddProductRequest
import com.mohalim.edokan.core.model.network.AddResponse
import com.mohalim.edokan.core.model.network.MarketplacesResponse
import com.mohalim.edokan.core.model.network.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SellerApiService{
    @GET("/Development/User/get-user-by-id")
    suspend fun getMarketplaceById(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    /**
     * Get Marketplaces for specific Seller
     */
    @GET("/Development/Marketplace/{city_id}/{marketplace_owner_id}")
    suspend fun getMarketplacesByUserAndCity(
        @Header("Authorization") token: String,
        @Path("city_id") cityId: Int,
        @Path("marketplace_owner_id") marketplaceOwnerId: String)
    : Response<MarketplacesResponse>

    @POST("/Development/Marketplace/add-marketplace")
    suspend fun addMarketplace(
        @Header("Authorization") token: String,
        @Body data: MarketPlace
    ): Response<AddResponse>

    @POST("/Development/Product/add-new-product")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body data: AddProductRequest
    ): Response<AddResponse>
}