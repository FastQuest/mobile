package com.example.fastquest.data.repository

import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionSet
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.network.service.QuestionSetsApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository for question sets operations
 */
class QuestionSetsRepository(
    private val apiService: QuestionSetsApiService
) {
    
    /**
     * Get paginated list of question sets
     * @param page Page number
     * @param perPage Items per page
     * @param searchTerm Optional search term
     * @param isPrivate Optional privacy filter
     * @param includeRelations Whether to include user and questions
     * @return NetworkResult with paginated question sets or error
     */
    suspend fun getQuestionSets(
        page: Int = 1,
        perPage: Int = 10,
        searchTerm: String? = null,
        isPrivate: Boolean? = null,
        includeRelations: Boolean = true
    ): NetworkResult<PaginatedResponse<QuestionSet>> {
        return try {
            val include = if (includeRelations) "user,questions" else null
            val response = apiService.getQuestionSets(
                page = page,
                perPage = perPage,
                searchTerm = searchTerm,
                isPrivate = isPrivate,
                include = include
            )
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "No question sets found"
                    else -> "Failed to load question sets: ${e.message()}"
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
     * Get a specific question set by ID
     * @param id Question set ID
     * @param includeRelations Whether to include user and questions
     * @return NetworkResult with question set or error
     */
    suspend fun getQuestionSet(
        id: Int,
        includeRelations: Boolean = true
    ): NetworkResult<QuestionSet> {
        return try {
            val include = if (includeRelations) "user,questions" else null
            val questionSet = apiService.getQuestionSet(id, include)
            NetworkResult.Success(questionSet)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "Question set not found"
                    else -> "Failed to load question set: ${e.message()}"
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
     * Get all questions in a specific question set
     * @param id Question set ID
     * @return NetworkResult with list of questions or error
     */
    suspend fun getQuestionSetQuestions(id: Int): NetworkResult<List<Question>> {
        return try {
            val questions = apiService.getQuestionSetQuestions(id)
            NetworkResult.Success(questions)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "Question set not found"
                    else -> "Failed to load questions: ${e.message()}"
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
