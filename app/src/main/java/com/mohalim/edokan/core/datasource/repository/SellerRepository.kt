package com.mohalim.edokan.core.datasource.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.utils.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SellerRepository @Inject constructor(val firestore : FirebaseFirestore) {

    private val marketPlaceCollection = firestore.collection("Marketplaces")

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
}