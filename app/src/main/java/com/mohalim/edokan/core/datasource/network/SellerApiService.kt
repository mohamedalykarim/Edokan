package com.mohalim.edokan.core.datasource.network

import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.Product
import com.mohalim.edokan.core.model.network.AddResponse
import com.mohalim.edokan.core.model.network.MarketplacesResponse
import com.mohalim.edokan.core.model.network.ProductResponse
import com.mohalim.edokan.core.model.network.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface SellerApiService{
    /**
     * Get Marketplaces for specific Seller
     */
    @GET("/marketplaces/{city_id}/{marketplace_owner_id}")
    suspend fun getMarketplacesByUserAndCity(
        @Header("Authorization") token: String,
        @Path("city_id") cityId: Int,
        @Path("marketplace_owner_id") marketplaceOwnerId: String)
    : Response<MarketplacesResponse>

    @POST("/marketplaces/")
    suspend fun addMarketplace(
        @Header("Authorization") token: String,
        @Body data: MarketPlace
    ): Response<AddResponse>


    @GET("/products/get")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("marketplace_id") marketplaceId: Int,
        @Query("search") search: String
    ): Response<ProductResponse>

    @Multipart
    @POST("/products/")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part("product_name") productName :String,
        @Part("product_description") productDescription : String,
        @Part("product_price") productPrices : Double,
        @Part("product_quantity") productQuantity : Double,
        @Part("product_width") productWidth : Double,
        @Part("product_height") productHeight : Double,
        @Part("product_weight") productWeight : Double,
        @Part("product_length") productLength : Double,
        @Part("product_discount") productDiscount : Double,
        @Part("marketplace_id") marketplaceId : Int,
        @Part("marketplace_name") marketplaceName : String,
        @Part("marketplace_lat") marketplaceLat : Double,
        @Part("marketplace_lng") marketplaceLng : Double,
        @Part("product_owner_id") productOwnerId : String,
        @Part("date_added") dateAdded : Double,
        @Part("date_modified") dateModified : Double,
        @Part("categories") categories : List<Int>,
        @Part files: List<MultipartBody.Part>
    ): Response<AddResponse>


}