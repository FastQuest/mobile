package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.SubmissionsRepository
import com.example.fastquest.ui.state.PerformanceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Results screen
 * Manages performance metrics and submission history
 */
class ResultsViewModel(
    private val repository: SubmissionsRepository
) : ViewModel() {
    
    // Performance UI state
    private val _performanceState = MutableStateFlow(PerformanceUiState())
    val performanceState: StateFlow<PerformanceUiState> = _performanceState.asStateFlow()
    
    /**
     * Load performance metrics
     */
    fun loadPerformance(userId: String? = null) {
        viewModelScope.launch {
            _performanceState.value = _performanceState.value.copy(isLoading = true, error = null)
            
            // Load performance metrics
            when (val metricsResult = repository.getPerformance()) {
                is NetworkResult.Success -> {
                    // Load submissions
                    when (val submissionsResult = repository.getSubmissions(null, 1, 10)) {
                        is NetworkResult.Success -> {
                            _performanceState.value = _performanceState.value.copy(
                                performance = metricsResult.data,
                                submissions = submissionsResult.data.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is NetworkResult.Error -> {
                            _performanceState.value = _performanceState.value.copy(
                                performance = metricsResult.data,
                                isLoading = false,
                                error = submissionsResult.message
                            )
                        }
                        is NetworkResult.Loading -> {
                            _performanceState.value = _performanceState.value.copy(isLoading = true)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _performanceState.value = _performanceState.value.copy(
                        isLoading = false,
                        error = metricsResult.message
                    )
                }
                is NetworkResult.Loading -> {
                    _performanceState.value = _performanceState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    /**
     * Refresh all data
     */
    fun refresh() {
        loadPerformance()
    }
    
    /**
     * Calculate accuracy percentage
     */
    fun calculateAccuracy(correct: Int, total: Int): Float {
        return if (total > 0) (correct.toFloat() / total.toFloat()) * 100 else 0f
    }
}

// Made with Bob
