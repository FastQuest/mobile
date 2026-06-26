package com.example.fastquest.data.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request model for user registration
 */
@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "name")
    val name: String,
    
    @Json(name = "email")
    val email: String,
    
    @Json(name = "password")
    val password: String
)

// Made with Bob
