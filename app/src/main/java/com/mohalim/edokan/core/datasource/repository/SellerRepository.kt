package com.mohalim.edokan.core.datasource.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mohalim.edokan.core.datasource.network.SellerApiService
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.Product
import com.mohalim.edokan.core.model.network.ProductByOwnerRequest
import com.mohalim.edokan.core.utils.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SellerRepository @Inject constructor(val firestore : FirebaseFirestore,val sellerApiService: SellerApiService) {

    private val marketPlaceCollection = firestore.collection("Marketplaces")

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
            Log.d("TAG", "addMarketPlace: " + response)
            if (response.isSuccessful) {
                Log.d("TAG", "addMarketplace isSuccessful")
                emit(Resource.Success(true))
            } else {
                Log.d("TAG", "addMarketplace else "+ response.body())

                emit(Resource.Error(message = response.body()?.message.toString()))
            }
        }catch (e:Exception){
            Log.d("TAG", "addMarketplace Exception "+ e.message)

            emit(Resource.Error("Catch Error : "+e.message))
        }
    }.flowOn(Dispatchers.IO)



    fun getApprovedMarketPlaces(token: String, cityId: Int, marketplaceOwnerId: String): Flow<Resource<out List<MarketPlace>>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.getMarketplacesByUserAndCity(token, cityId, marketplaceOwnerId)
            Log.d("TAG", "getApprovedMarketPlaces: " + response.body())
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
            sellerApiService.getProducts(token, productRequest)
        }catch (e : Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun addProduct(product: Product): Flow<Resource<out Boolean>> = flow {
        emit(Resource.Loading())

        try {
            // Create a new document reference without specifying a document ID
            val documentRef = firestore.collection("Products").document()

            // Set the productId to the document ID
            val productWithId = product.copy(productId = documentRef.id)

            // Add the product to Firestore with the generated productId
            documentRef.set(productWithId).await()

            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.d("TAG", "addProduct: " + e.message)
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error", false))
        }
    }.flowOn(Dispatchers.IO)

}