package com.example.fastquest.data.network

import com.example.fastquest.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor that adds authentication token to requests
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val token = tokenManager.cachedAccessToken
        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(request)
    }
}