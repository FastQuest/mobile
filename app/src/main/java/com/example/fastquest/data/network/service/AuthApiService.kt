package com.example.fastquest.data.network.service

import com.example.fastquest.data.model.request.LoginRequest
import com.example.fastquest.data.model.request.RegisterRequest
import com.example.fastquest.data.model.response.AuthResponse
import com.example.fastquest.data.model.response.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit API service for authentication endpoints
 */
interface AuthApiService {
    
    /**
     * Login user with email and password
     * @param request Login credentials
     * @return AuthResponse with access token and user info
     */
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    /**
     * Register new user
     * @param request Registration data (name, email, password)
     * @return AuthResponse with access token and user info
     */
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    /**
     * Get current authenticated user profile
     * Requires Bearer token in Authorization header
     * @return User profile data
     */
    @GET("users/me")
    suspend fun getCurrentUser(): User
}

// Made with Bob
