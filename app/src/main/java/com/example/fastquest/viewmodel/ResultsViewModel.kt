package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.model.response.OverallPerformance
import com.example.fastquest.data.model.response.PerformanceMetrics
import com.example.fastquest.data.model.response.Submission
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.SubmissionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Results screen
 * Manages performance metrics and submission history
 */
class ResultsViewModel : ViewModel() {
    
    private val repository = SubmissionsRepository(
        ApiClient.submissionsService,
        ApiClient.answersService
    )
    
    // Performance metrics state
    private val _performanceMetrics = MutableStateFlow<NetworkResult<PerformanceMetrics>>(NetworkResult.Loading())
    val performanceMetrics: StateFlow<NetworkResult<PerformanceMetrics>> = _performanceMetrics.asStateFlow()
    
    // Overall performance state
    private val _overallPerformance = MutableStateFlow<NetworkResult<OverallPerformance>>(NetworkResult.Loading())
    val overallPerformance: StateFlow<NetworkResult<OverallPerformance>> = _overallPerformance.asStateFlow()
    
    // Submissions history state
    private val _submissions = MutableStateFlow<NetworkResult<List<Submission>>>(NetworkResult.Loading())
    val submissions: StateFlow<NetworkResult<List<Submission>>> = _submissions.asStateFlow()
    
    // Current submission details
    private val _currentSubmission = MutableStateFlow<NetworkResult<Submission>?>(null)
    val currentSubmission: StateFlow<NetworkResult<Submission>?> = _currentSubmission.asStateFlow()
    
    // Pagination
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()
    
    /**
     * Load performance metrics
     */
    fun loadPerformanceMetrics() {
        viewModelScope.launch {
            _performanceMetrics.value = NetworkResult.Loading()
            _performanceMetrics.value = repository.getPerformance()
        }
    }
    
    /**
     * Load overall performance summary
     */
    fun loadOverallPerformance() {
        viewModelScope.launch {
            _overallPerformance.value = NetworkResult.Loading()
            _overallPerformance.value = repository.getOverallPerformance()
        }
    }
    
    /**
     * Load submissions history
     */
    fun loadSubmissions(questionSetId: Int? = null, page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh || _submissions.value !is NetworkResult.Success) {
                _submissions.value = NetworkResult.Loading()
            }
            
            val result = repository.getSubmissions(questionSetId, page, perPage = 10)
            
            when (result) {
                is NetworkResult.Success -> {
                    _submissions.value = NetworkResult.Success(result.data.data)
                    _currentPage.value = result.data.pagination.currentPage
                    _totalPages.value = result.data.pagination.totalPages
                }
                is NetworkResult.Error -> {
                    _submissions.value = result
                }
                is NetworkResult.Loading -> {
                    _submissions.value = result
                }
            }
        }
    }
    
    /**
     * Load specific submission details
     */
    fun loadSubmission(submissionId: Int) {
        viewModelScope.launch {
            _currentSubmission.value = NetworkResult.Loading()
            _currentSubmission.value = repository.getSubmission(submissionId)
        }
    }
    
    /**
     * Load all performance data
     */
    fun loadAllPerformanceData() {
        loadPerformanceMetrics()
        loadOverallPerformance()
        loadSubmissions()
    }
    
    /**
     * Refresh all data
     */
    fun refresh() {
        loadAllPerformanceData()
    }
    
    /**
     * Go to next page of submissions
     */
    fun nextPage() {
        val current = _currentPage.value
        val total = _totalPages.value
        if (current < total) {
            loadSubmissions(page = current + 1)
        }
    }
    
    /**
     * Go to previous page of submissions
     */
    fun previousPage() {
        val current = _currentPage.value
        if (current > 1) {
            loadSubmissions(page = current - 1)
        }
    }
    
    /**
     * Go to specific page
     */
    fun goToPage(page: Int) {
        if (page in 1.._totalPages.value) {
            loadSubmissions(page = page)
        }
    }
    
    /**
     * Clear current submission
     */
    fun clearCurrentSubmission() {
        _currentSubmission.value = null
    }
    
    /**
     * Calculate accuracy percentage
     */
    fun calculateAccuracy(correct: Int, total: Int): Float {
        return if (total > 0) (correct.toFloat() / total.toFloat()) * 100 else 0f
    }
    
    /**
     * Get performance trend text
     */
    fun getPerformanceTrendText(trend: String?): String {
        return when (trend?.lowercase()) {
            "improving" -> "📈 Improving"
            "declining" -> "📉 Declining"
            "stable" -> "➡️ Stable"
            else -> "➖ No data"
        }
    }
}

// Made with Bob
