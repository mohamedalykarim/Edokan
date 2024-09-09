package com.mohalim.edokan.core.datasource.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mohalim.edokan.R
import com.mohalim.edokan.core.datasource.network.CategoryApiService
import com.mohalim.edokan.core.datasource.network.SellerApiService
import com.mohalim.edokan.core.model.Category
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.Product
import com.mohalim.edokan.core.model.network.AddProductRequest
import com.mohalim.edokan.core.model.network.ProductByOwnerRequest
import com.mohalim.edokan.core.utils.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SellerRepository @Inject constructor(
    val firestore : FirebaseFirestore,
    val sellerApiService: SellerApiService,
    val categoryApiService: CategoryApiService
){


    fun getCategoriesByName(token: String, searchQuery: String): Flow<Resource<out List<Category>>> = flow{
        emit(Resource.Loading())
        try {
            val response = categoryApiService.getCategoriesByName(token, searchQuery)
            if(response.isSuccessful){
                val categories : MutableList<Category> = ArrayList()
                response.body()?.result?.forEach {
                    categories.add(it)
                }
                emit(Resource.Success(categories))
            }else{
                emit(Resource.Error(message = response.errorBody()?.string()+""))
            }

        }catch (e : Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Read User data by user ID
     */

    fun addMarketPlace(
        token: String,
        name: String,
        lat:Double,
        lng:Double,
        cityId: Int,
        cityName:String,
        ownerId : String,
        isApproved: Int): Flow<Resource<out Boolean>> = flow {

        try{
            val marketPlace = MarketPlace(
                marketplaceName = name,
                lat = lat,
                lng = lng,
                cityId = cityId,
                city = cityName,
                marketplaceOwnerId = ownerId,
                isApproved = isApproved
            )
            val response = sellerApiService.addMarketplace(token, marketPlace)
            if (response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error(errorBody.toString()))
            }
        }catch (e:Exception){
            emit(Resource.Error(""+ e.message))
        }
    }.flowOn(Dispatchers.IO)

    fun getApprovedMarketPlaces(token: String, cityId: Int, marketplaceOwnerId: String): Flow<Resource<out List<MarketPlace>>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.getMarketplacesByUserAndCity(token, cityId, marketplaceOwnerId)
            if (response.isSuccessful){
                val marketplaces : MutableList<MarketPlace> = ArrayList()
                response.body()?.result?.forEach {
                    marketplaces.add(it)
                }
                emit(Resource.Success(marketplaces))
            }else{
                emit(Resource.Error(message = response.body()?.message.toString()))
            }



        } catch (e: Exception) {
            Log.d("TAG", "getApprovedMarketPlaces: " + e.message)

            emit(Resource.Error("Failed to fetch data: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getProducts(token: String, searchQuery: String, limit: Long, marketplaceOwnerId: String): Flow<Resource<out List<Product>>> = flow<Resource<out List<Product>>> {
        emit(Resource.Loading())
        try {
            val productRequest = ProductByOwnerRequest(searchQuery, limit, marketplaceOwnerId)
            //sellerApiService.getProducts(token, productRequest)
        }catch (e : Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)


    fun addProduct(context: Context, token: String, product: Product): Flow<Resource<out Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val addProductRequest = AddProductRequest(
                productName = product.productName,
                productDescription = product.productDescription,
                productPrice = product.productPrice,
                productQuantity = product.productQuantity,
                productWidth = product.productWidth,
                productHeight = product.productHeight,
                productWeight = product.productWeight,
                productLength = product.productLength,
                productDiscount = product.productDiscount,
                marketplaceId = product.marketPlaceId,
                marketplaceName = product.marketPlaceName,
                marketplaceLat = product.marketPlaceLat,
                marketplaceLng = product.marketPlaceLng,
                dateAdded = product.dateAdded,
                dateModifier = product.dateModified
            )

            val holderUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(context.resources.getResourcePackageName(R.drawable.image_placeholder))
                .appendPath(context.resources.getResourceTypeName(R.drawable.image_placeholder))
                .appendPath(context.resources.getResourceEntryName(R.drawable.image_placeholder))
                .build()


            val imagePaths = listOf(product.productImageUrl, product.productImage1Url, product.productImage2Url, product.productImage3Url,product.productImage4Url)

            // Create MultipartBody.Part for each file
            val fileParts = imagePaths.map { imagePath ->
                val file = File(imagePath)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("files", file.name, requestFile)
            }

            val response = sellerApiService.addProduct(token, addProductRequest, fileParts)
            if (response.isSuccessful) {
                Log.d("TAG", "addProduct: Success")

                emit(Resource.Success(true))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.d("TAG", "addProduct errorBody: " + errorBody)
                emit(Resource.Error(errorBody.toString()))
            }

        } catch (e: Exception) {
            Log.d("TAG", "addProduct: " + e.message)
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error", false))
        }
    }.flowOn(Dispatchers.IO)

}