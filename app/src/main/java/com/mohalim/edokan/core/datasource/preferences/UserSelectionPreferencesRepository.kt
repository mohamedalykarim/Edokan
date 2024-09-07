package com.mohalim.edokan.core.datasource.preferences

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single


class UserSelectionPreferencesRepository(private val context: Context) {

    // Keys for storing data
    companion object {
        val SELECTED_MARKETPLACE_ID = intPreferencesKey("selected_marketplace_Id")
        val SELECTED_MARKETPLACE_NAME = stringPreferencesKey("selected_marketplace_name")
        val SELECTED_MARKETPLACE_LAT = doublePreferencesKey("selected_marketplace_lat")
        val SELECTED_MARKETPLACE_LNG = doublePreferencesKey("selected_marketplace_lng")
    }

    suspend fun saveSelectedMarketplaceData(selectedMarketplaceId: Int, selectedMarketplaceName: String, selectedMarketplaceLat: Double, selectedMarketplaceLng: Double){
        context.dataStore.edit {
            it[SELECTED_MARKETPLACE_ID] = selectedMarketplaceId
            it[SELECTED_MARKETPLACE_NAME] = selectedMarketplaceName
            it[SELECTED_MARKETPLACE_LAT] = selectedMarketplaceLat
            it[SELECTED_MARKETPLACE_LNG] = selectedMarketplaceLng
        }
    }

    // set Selected Marketplace Id
    suspend fun setSelectedMarketplaceId(selectedMarketplaceId: Int) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_MARKETPLACE_ID] = selectedMarketplaceId
        }
    }

    val selectedMarketplaceIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_MARKETPLACE_ID]
        }

    suspend fun getSelectedMarketplaceId(): Int? {
        val marketPlaceId: Int? = context.dataStore.data
            .map { preferences -> preferences[SELECTED_MARKETPLACE_ID] }
            .first()

        return marketPlaceId
    }


    suspend fun setSelectedMarketplaceName(selectedMarketplace: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_MARKETPLACE_NAME] = selectedMarketplace
        }
    }

    val selectedMarketplaceNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_MARKETPLACE_NAME]
        }



    suspend fun getSelectedMarketplaceName(): String? {
        val marketPlace: String? = context.dataStore.data
            .map { preferences -> preferences[SELECTED_MARKETPLACE_NAME] }
            .first()

        return marketPlace
    }

    // set Selected Marketplace Lat
    suspend fun setSelectedMarketplaceLat(selectedMarketplacelat: Double) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_MARKETPLACE_LAT] = selectedMarketplacelat
        }
    }

    val selectedMarketplaceLatFlow: Flow<Double?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_MARKETPLACE_LAT]
        }

    suspend fun getSelectedMarketplaceLat(): Double? {
        val marketPlaceLat: Double? = context.dataStore.data
            .map { preferences -> preferences[SELECTED_MARKETPLACE_LAT] }
            .first()

        return marketPlaceLat
    }

    // set Selected Marketplace Lng
    suspend fun setSelectedMarketplaceLng(selectedMarketplaceLng: Double) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_MARKETPLACE_LNG] = selectedMarketplaceLng
        }
    }

    val selectedMarketplaceLngFlow: Flow<Double?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_MARKETPLACE_LNG]
        }

    suspend fun getSelectedMarketplaceLng(): Double? {
        val marketPlaceLng: Double? = context.dataStore.data
            .map { preferences -> preferences[SELECTED_MARKETPLACE_LNG] }
            .first()

        return marketPlaceLng
    }

    suspend fun clearSelectedMarketplace() {
        saveSelectedMarketplaceData(0, "", 0.0, 0.0)
    }


}
