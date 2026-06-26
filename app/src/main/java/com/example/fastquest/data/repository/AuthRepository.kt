package com.example.fastquest.data.repository

import com.example.fastquest.data.local.TokenManager
import com.example.fastquest.data.model.request.LoginRequest
import com.example.fastquest.data.model.request.RegisterRequest
import com.example.fastquest.data.model.response.AuthResponse
import com.example.fastquest.data.model.response.User
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.network.service.AuthApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository for authentication operations
 */
class AuthRepository(
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) {
    
    /**
     * Login user with email and password
     * @param email User email
     * @param password User password
     * @return NetworkResult with AuthResponse or error
     */
    suspend fun login(email: String, password: String): NetworkResult<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            // Save authentication data
            tokenManager.saveAuthData(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                userId = response.userId
            )
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Invalid email or password"
                    422 -> "Invalid email format"
                    else -> "Login failed: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Register new user
     * @param name User's full name
     * @param email User email
     * @param password User password
     * @return NetworkResult with AuthResponse or error
     */
    suspend fun register(name: String, email: String, password: String): NetworkResult<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            // Save authentication data
            tokenManager.saveAuthData(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                userId = response.userId
            )
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    409 -> "Email already in use"
                    422 -> "Email domain not allowed"
                    400 -> "Invalid registration data"
                    else -> "Registration failed: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Get current authenticated user profile
     * @return NetworkResult with User or error
     */
    suspend fun getCurrentUser(): NetworkResult<User> {
        return try {
            val user = apiService.getCurrentUser()
            NetworkResult.Success(user)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "User not found"
                    else -> "Failed to get user profile: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Logout user by clearing authentication data
     */
    suspend fun logout() {
        tokenManager.clearAuthData()
    }
    
    /**
     * Check if user is logged in
     * @return Flow<Boolean> indicating login status
     */
    fun isLoggedIn(): Flow<Boolean> {
        return tokenManager.isLoggedIn()
    }
    
    /**
     * Get current user ID
     * @return Flow<Int?> with user ID or null
     */
    fun getUserId(): Flow<Int?> {
        return tokenManager.getUserId()
    }
}

// Made with Bob
