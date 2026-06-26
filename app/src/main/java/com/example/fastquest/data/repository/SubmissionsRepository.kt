package com.example.fastquest.data.repository

import com.example.fastquest.data.model.request.CreateSubmissionRequest
import com.example.fastquest.data.model.response.Answer
import com.example.fastquest.data.model.response.OverallPerformance
import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.PerformanceMetrics
import com.example.fastquest.data.model.response.Submission
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.network.service.AnswersApiService
import com.example.fastquest.data.network.service.SubmissionsApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository for submissions and performance operations
 */
class SubmissionsRepository(
    private val submissionsApiService: SubmissionsApiService,
    private val answersApiService: AnswersApiService
) {
    
    /**
     * Get paginated list of user submissions
     * @param questionSetId Optional filter by question set ID
     * @param page Page number
     * @param perPage Items per page
     * @return NetworkResult with paginated submissions or error
     */
    suspend fun getSubmissions(
        questionSetId: Int? = null,
        page: Int = 1,
        perPage: Int = 10
    ): NetworkResult<PaginatedResponse<Submission>> {
        return try {
            val response = submissionsApiService.getSubmissions(questionSetId, page, perPage)
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "No submissions found"
                    else -> "Failed to load submissions: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Get a specific submission by ID
     * @param id Submission ID
     * @return NetworkResult with submission or error
     */
    suspend fun getSubmission(id: Int): NetworkResult<Submission> {
        return try {
            val submission = submissionsApiService.getSubmission(id)
            NetworkResult.Success(submission)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "Submission not found"
                    else -> "Failed to load submission: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Create a new submission with user answers
     * @param questionSetId Question set ID
     * @param answers List of user answers
     * @return NetworkResult with created submission or error
     */
    suspend fun createSubmission(
        questionSetId: Int,
        answers: List<Answer>
    ): NetworkResult<Submission> {
        return try {
            val request = CreateSubmissionRequest(questionSetId, answers)
            val submission = submissionsApiService.createSubmission(request)
            NetworkResult.Success(submission)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    400 -> "Invalid submission data"
                    404 -> "Question set not found"
                    else -> "Failed to submit answers: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Get user's performance metrics
     * @return NetworkResult with performance metrics or error
     */
    suspend fun getPerformance(): NetworkResult<PerformanceMetrics> {
        return try {
            val performance = answersApiService.getPerformance()
            NetworkResult.Success(performance)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "No performance data found"
                    else -> "Failed to load performance: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
    
    /**
     * Get user's overall performance summary
     * @return NetworkResult with overall performance or error
     */
    suspend fun getOverallPerformance(): NetworkResult<OverallPerformance> {
        return try {
            val performance = answersApiService.getOverallPerformance()
            NetworkResult.Success(performance)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "No performance data found"
                    else -> "Failed to load overall performance: ${e.message()}"
                },
                code = e.code()
            )
        } catch (e: IOException) {
            NetworkResult.Error("Network error. Please check your connection.")
        } catch (e: Exception) {
            NetworkResult.Error("An unexpected error occurred: ${e.message}")
        }
    }
}

// Made with Bob
