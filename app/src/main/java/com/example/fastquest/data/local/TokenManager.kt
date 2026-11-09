package com.example.fastquest.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.concurrent.atomic.AtomicReference
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
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

    @Volatile
    private var accessTokenCache = AtomicReference<String?>(null)

    val cachedAccessToken: String?
        get() = accessTokenCache.get()

    fun cacheAccessToken(token: String?) {
        accessTokenCache.set(token)
    }
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private const val ENCRYPTED_PREFIX = "enc:v1:"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "fastquest_auth_tokens"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
    }
    
    /**
     * Save access token to DataStore
     */
    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encrypt(token)
        }
        cacheAccessToken(token)
    }
    
    /**
     * Save refresh token to DataStore
     */
    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = encrypt(token)
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
            preferences[ACCESS_TOKEN_KEY] = encrypt(accessToken)
            preferences[REFRESH_TOKEN_KEY] = encrypt(refreshToken)
            preferences[USER_ID_KEY] = userId.toString()
        }
        cacheAccessToken(accessToken)
    }
    
    /**
     * Get access token as Flow
     */
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]?.let(::decrypt)
        }
    }
    
    /**
     * Get refresh token as Flow
     */
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]?.let(::decrypt)
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
            preferences[ACCESS_TOKEN_KEY]?.let(::decrypt) != null
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
        cacheAccessToken(null)
    }
    
    /**
     * Clear only access token (for token refresh scenarios)
     */
    suspend fun clearAccessToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
        cacheAccessToken(null)
    }

    suspend fun migrateLegacyTokens() {
        context.dataStore.edit { preferences ->
            listOf(ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY).forEach { key ->
                val value = preferences[key]
                if (value != null && !value.startsWith(ENCRYPTED_PREFIX)) {
                    preferences[key] = encrypt(value)
                }
            }
        }
    }

    private fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        }
        val encrypted = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))
        val payload = cipher.iv + encrypted
        return ENCRYPTED_PREFIX + Base64.encodeToString(payload, Base64.NO_WRAP)
    }

    private fun decrypt(value: String): String? {
        if (!value.startsWith(ENCRYPTED_PREFIX)) return value
        return runCatching {
            val payload = Base64.decode(value.removePrefix(ENCRYPTED_PREFIX), Base64.NO_WRAP)
            val iv = payload.copyOfRange(0, GCM_IV_LENGTH)
            val encrypted = payload.copyOfRange(GCM_IV_LENGTH, payload.size)
            val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(GCM_TAG_LENGTH, iv))
            }
            String(cipher.doFinal(encrypted), StandardCharsets.UTF_8)
        }.getOrNull()
    }

    @Synchronized
    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE).run {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
            generateKey()
        }
    }
}