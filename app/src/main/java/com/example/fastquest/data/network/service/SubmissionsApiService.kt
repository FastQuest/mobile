package com.example.fastquest.data.network.service

import com.example.fastquest.data.model.request.CreateSubmissionRequest
import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.Submission
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for submissions endpoints
 * All endpoints require authentication
 */
interface SubmissionsApiService {
    
    /**
     * Get paginated list of user submissions
     * @param questionSetId Filter by question set ID
     * @param page Page number (default: 1)
     * @param perPage Items per page (default: 10)
     * @return Paginated response with submissions
     */
    @GET("submissions")
    suspend fun getSubmissions(
        @Query("question_set_id") questionSetId: Int? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10
    ): PaginatedResponse<Submission>
    
    /**
     * Get a specific submission by ID
     * @param id Submission ID
     * @return Submission details with answers
     */
    @GET("submissions/{id}")
    suspend fun getSubmission(
        @Path("id") id: Int
    ): Submission
    
    /**
     * Create a new submission with user answers
     * @param request Submission data with answers
     * @return Created submission with score
     */
    @POST("submissions")
    suspend fun createSubmission(
        @Body request: CreateSubmissionRequest
    ): Submission
}

// Made with Bob
