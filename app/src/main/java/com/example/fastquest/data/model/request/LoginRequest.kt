package com.example.fastquest.data.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request model for user login
 */
@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email")
    val email: String,
    
    @Json(name = "password")
    val password: String
)

// Made with Bob
