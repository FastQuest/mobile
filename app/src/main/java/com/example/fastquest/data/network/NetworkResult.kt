package com.example.fastquest.data.network

/**
 * A sealed class representing the result of a network operation.
 * Used to handle loading, success, and error states in a type-safe manner.
 */
sealed class NetworkResult<out T> {
    /**
     * Represents a successful network operation with data.
     * @param data The data returned from the operation
     */
    data class Success<T>(val data: T) : NetworkResult<T>()
    
    /**
     * Represents a failed network operation.
     * @param message The error message
     * @param code The HTTP status code (if applicable)
     */
    data class Error<T>(
        val message: String,
        val code: Int? = null
    ) : NetworkResult<T>()
    
    /**
     * Represents a loading state.
     * @param isLoading Whether the operation is currently loading
     */
    data class Loading<T>(val isLoading: Boolean = true) : NetworkResult<T>()
}

/**
 * Extension function to check if the result is successful
 */
fun <T> NetworkResult<T>.isSuccess(): Boolean = this is NetworkResult.Success

/**
 * Extension function to check if the result is an error
 */
fun <T> NetworkResult<T>.isError(): Boolean = this is NetworkResult.Error

/**
 * Extension function to check if the result is loading
 */
fun <T> NetworkResult<T>.isLoading(): Boolean = this is NetworkResult.Loading

/**
 * Extension function to get data or null
 */
fun <T> NetworkResult<T>.getDataOrNull(): T? {
    return when (this) {
        is NetworkResult.Success -> data
        else -> null
    }
}

/**
 * Extension function to get error message or null
 */
fun <T> NetworkResult<T>.getErrorOrNull(): String? {
    return when (this) {
        is NetworkResult.Error -> message
        else -> null
    }
}

// Made with Bob
