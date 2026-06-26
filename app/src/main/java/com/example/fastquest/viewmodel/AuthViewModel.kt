package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.model.response.AuthResponse
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication screens (Login and Register)
 */
class AuthViewModel : ViewModel() {
    
    private val repository = AuthRepository(
        apiService = ApiClient.authService,
        tokenManager = ApiClient.getTokenManager()
    )
    
    // Authentication state
    private val _authState = MutableStateFlow<NetworkResult<AuthResponse>?>(null)
    val authState: StateFlow<NetworkResult<AuthResponse>?> = _authState.asStateFlow()
    
    // Login status
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    init {
        // Observe login status
        viewModelScope.launch {
            repository.isLoggedIn().collect { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }
    }
    
    /**
     * Login user with email and password
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = NetworkResult.Loading()
            _authState.value = repository.login(email, password)
        }
    }
    
    /**
     * Register new user
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = NetworkResult.Loading()
            _authState.value = repository.register(name, email, password)
        }
    }
    
    /**
     * Logout current user
     */
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = null
        }
    }
    
    /**
     * Clear authentication state (for navigation)
     */
    fun clearAuthState() {
        _authState.value = null
    }
    
    /**
     * Validate email format
     */
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Validate password strength
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}

// Made with Bob
