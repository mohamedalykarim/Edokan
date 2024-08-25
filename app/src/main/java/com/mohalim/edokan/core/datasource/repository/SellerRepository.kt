package com.mohalim.edokan.core.datasource.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.Product
import com.mohalim.edokan.core.utils.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SellerRepository @Inject constructor(val firestore : FirebaseFirestore) {

    private val marketPlaceCollection = firestore.collection("Marketplaces")

    /**
     * Read User data by user ID
     */


    fun addMarketPlace(marketPlace: MarketPlace): Flow<Resource<out String>> = flow {
        try {
            emit(Resource.Loading())
            val docRef = marketPlaceCollection.document()
            val marketPlaceWithId = marketPlace.copy(marketplaceId = docRef.id)
            docRef.set(marketPlaceWithId).await()
            emit(Resource.Success("Marketplace added successfully, Please wait for admin approval"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)



    fun getApprovedMarketPlaces(cityId: Int, marketplaceOwnerId: String): Flow<Resource<out List<MarketPlace>>> = flow {
        Log.d("TAG", "getApprovedMarketPlaces: "+cityId+" "+marketplaceOwnerId)
        try {
            emit(Resource.Loading())

            val querySnapshot = firestore.collection("Marketplaces")
                .whereEqualTo("approved", true)
                .whereEqualTo("cityId", cityId)
                .whereEqualTo("marketplaceOwnerId", marketplaceOwnerId)
                .get()
                .await()

            val marketPlaces = querySnapshot.documents.mapNotNull { document ->
                document.toObject(MarketPlace::class.java)?.copy(marketplaceId = document.id)
            }
            emit(Resource.Success(marketPlaces))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch data: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getProducts(searchQuery: String, limit: Long): List<Product> {
        return try {
            var query = firestore.collection("Products")
                .orderBy("productName")
                .limit(limit)

            if (searchQuery.isNotEmpty()) {
                query = query.whereGreaterThanOrEqualTo("productName", searchQuery)
                    .whereLessThanOrEqualTo("productName", "$searchQuery\uf8ff")
                    .whereEqualTo("productStatus", true)
            }

            val querySnapshot = query.get().await()
            querySnapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

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