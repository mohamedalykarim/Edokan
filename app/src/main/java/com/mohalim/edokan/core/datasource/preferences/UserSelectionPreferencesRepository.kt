package com.mohalim.edokan.core.datasource.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single


class UserSelectionPreferencesRepository(private val context: Context) {

    // Keys for storing data
    companion object {
        val SELECTED_MARKETPLACE_ID = stringPreferencesKey("selected_marketplace_ID")
        val SELECTED_MARKETPLACE_NAME = stringPreferencesKey("selected_marketplace_name")
    }

    // Save user details
    suspend fun setSelectedMarketplaceId(selectedMarketplaceId: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_MARKETPLACE_ID] = selectedMarketplaceId
        }
    }

    val selectedMarketplaceIdFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_MARKETPLACE_ID]
        }

    suspend fun getSelectedMarketplaceId(): String? {
        val marketPlaceId: String? = context.dataStore.data
            .map { preferences -> preferences[SELECTED_MARKETPLACE_ID] }
            .single()

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
            .single()

        return marketPlace
    }


}
