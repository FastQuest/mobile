package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.AuthRepository
import com.example.fastquest.ui.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication screens (Login and Register)
 */
class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    
    // Authentication UI state
    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()
    
    init {
        // Observe login status
        viewModelScope.launch {
            repository.isLoggedIn().collect { loggedIn ->
                _authState.value = _authState.value.copy(isAuthenticated = loggedIn)
            }
        }
    }
    
    /**
     * Login user with email and password
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            when (val result = repository.login(email, password)) {
                is NetworkResult.Success -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _authState.value = _authState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    /**
     * Register new user
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            when (val result = repository.register(name, email, password)) {
                is NetworkResult.Success -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _authState.value = _authState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    /**
     * Logout current user
     */
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthUiState()
        }
    }
    
    /**
     * Clear authentication state (for navigation)
     */
    fun clearAuthState() {
        _authState.value = AuthUiState()
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
