package com.example.fastquest.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response model for authentication (login/register)
 */
@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "access_token")
    val accessToken: String,
    
    @Json(name = "refresh_token")
    val refreshToken: String,
    
    @Json(name = "expires_in")
    val expiresIn: Int,
    
    @Json(name = "user_id")
    val userId: Int
)

// Made with Bob
