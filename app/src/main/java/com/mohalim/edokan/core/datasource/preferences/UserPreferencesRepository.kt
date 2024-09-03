package com.mohalim.edokan.core.datasource.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferencesRepository(private val context: Context) {

    // Keys for storing data
    companion object {
        val USER_ID = stringPreferencesKey("uid")
        val USERNAME_KEY = stringPreferencesKey("username")
        val PHONE_NUMBER_KEY = stringPreferencesKey("phone_number")
        val IMAGE_URL_KEY = stringPreferencesKey("image_url")
        val ROLE_KEY = stringPreferencesKey("role")
        val CITY_ID = intPreferencesKey("city_id")
        val CITY = stringPreferencesKey("city")
    }

    // Save user details
    suspend fun saveUserDetails(uid: String, username: String, phoneNumber: String, imageUrl: String, role: String, cityId : Int, city : String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = uid
            preferences[USERNAME_KEY] = username
            preferences[PHONE_NUMBER_KEY] = phoneNumber
            preferences[IMAGE_URL_KEY] = imageUrl
            preferences[ROLE_KEY] = role
            preferences[CITY_ID] = cityId
            preferences[CITY] = city
        }
    }

    suspend fun setRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun clearUserDetails() {
        saveUserDetails("", "", "", "", "", 0, "")
    }

    // Retrieve user details
    val userIdFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID]
        }

    val usernameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }

    val phoneNumberFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PHONE_NUMBER_KEY]
        }

    val imageUrlFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[IMAGE_URL_KEY]
        }

    val roleFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[ROLE_KEY]
        }

    val cityIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[CITY_ID]
        }

    val cityFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[CITY]
        }

    suspend fun getRole(): String? {
        val role: String? = context.dataStore.data
            .map { preferences -> preferences[ROLE_KEY] }
            .single()

        return role
    }

    suspend fun getCityId(): Int? {
        val cityId: Int? = context.dataStore.data
            .map { preferences -> preferences[CITY_ID] }
            .single()

        return cityId
    }

    suspend fun getCity(): String? {
        val city: String? = context.dataStore.data
            .map { preferences -> preferences[CITY] }
            .single()

        return city
    }
}
