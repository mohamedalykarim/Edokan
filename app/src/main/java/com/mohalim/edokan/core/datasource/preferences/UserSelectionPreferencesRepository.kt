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
        val SELECTED_MARKETPLACE = stringPreferencesKey("selected_marketplace")
    }

    // Save user details
    suspend fun setSelectedMarketplace(selectedMarketplace: String, ) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_MARKETPLACE] = selectedMarketplace
        }
    }

    // Retrieve user details
    val selectedMarketplace: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_MARKETPLACE]
        }

    suspend fun getSelectedMarketplace(): String? {
        val city: String? = context.dataStore.data
            .map { preferences -> preferences[SELECTED_MARKETPLACE] }
            .single()

        return city
    }
}
