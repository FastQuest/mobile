package com.example.fastquest.data.network.service

import com.example.fastquest.data.model.response.OverallPerformance
import com.example.fastquest.data.model.response.PerformanceMetrics
import retrofit2.http.GET

/**
 * Retrofit API service for answers/performance endpoints
 * All endpoints require authentication
 */
interface AnswersApiService {
    
    /**
     * Get user's performance metrics
     * Includes total questions answered, correct answers, accuracy, and breakdown by subject
     * @return Performance metrics
     */
    @GET("answers/performance")
    suspend fun getPerformance(): PerformanceMetrics
    
    /**
     * Get user's overall performance summary
     * Includes total submissions, average score, best score, and recent trend
     * @return Overall performance metrics
     */
    @GET("answers/overall-performance")
    suspend fun getOverallPerformance(): OverallPerformance
}

// Made with Bob
