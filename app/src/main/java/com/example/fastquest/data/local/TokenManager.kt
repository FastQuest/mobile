package com.example.fastquest.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property to create DataStore instance
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

/**
 * Manages authentication tokens using DataStore for secure storage
 */
class TokenManager(private val context: Context) {
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }
    
    /**
     * Save access token to DataStore
     */
    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }
    
    /**
     * Save refresh token to DataStore
     */
    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }
    
    /**
     * Save user ID to DataStore
     */
    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId.toString()
        }
    }
    
    /**
     * Save all authentication data at once
     */
    suspend fun saveAuthData(accessToken: String, refreshToken: String, userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[USER_ID_KEY] = userId.toString()
        }
    }
    
    /**
     * Get access token as Flow
     */
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }
    
    /**
     * Get refresh token as Flow
     */
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }
    
    /**
     * Get user ID as Flow
     */
    fun getUserId(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]?.toIntOrNull()
        }
    }
    
    /**
     * Check if user is logged in (has access token)
     */
    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] != null
        }
    }
    
    /**
     * Clear all authentication data (logout)
     */
    suspend fun clearAuthData() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
        }
    }
    
    /**
     * Clear only access token (for token refresh scenarios)
     */
    suspend fun clearAccessToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}

// Made with Bob
