package com.example.fastquest.data.network

import com.example.fastquest.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor that adds authentication token to requests
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Get token synchronously (required by OkHttp interceptor)
        val token = runBlocking {
            tokenManager.getAccessToken().first()
        }
        
        // If token exists, add Authorization header
        val request = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(request)
    }
}

// Made with Bob
