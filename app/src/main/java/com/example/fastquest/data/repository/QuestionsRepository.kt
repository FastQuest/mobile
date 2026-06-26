package com.example.fastquest.data.repository

import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionOption
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.network.service.QuestionsApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository for questions operations
 */
class QuestionsRepository(
    private val apiService: QuestionsApiService
) {
    
    /**
     * Get paginated list of questions
     * @param page Page number
     * @param perPage Items per page
     * @return NetworkResult with paginated questions or error
     */
    suspend fun getQuestions(
        page: Int = 1,
        perPage: Int = 10
    ): NetworkResult<PaginatedResponse<Question>> {
        return try {
            val response = apiService.getQuestions(page, perPage)
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "No questions found"
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
    
    /**
     * Get a specific question by ID
     * @param id Question ID
     * @return NetworkResult with question or error
     */
    suspend fun getQuestion(id: Int): NetworkResult<Question> {
        return try {
            val question = apiService.getQuestion(id)
            NetworkResult.Success(question)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "Question not found"
                    else -> "Failed to load question: ${e.message()}"
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
     * Get all options for a specific question
     * @param questionId Question ID
     * @return NetworkResult with list of question options or error
     */
    suspend fun getQuestionOptions(questionId: Int): NetworkResult<List<QuestionOption>> {
        return try {
            val options = apiService.getQuestionOptions(questionId)
            NetworkResult.Success(options)
        } catch (e: HttpException) {
            NetworkResult.Error(
                message = when (e.code()) {
                    401 -> "Session expired. Please login again."
                    404 -> "Question options not found"
                    else -> "Failed to load question options: ${e.message()}"
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
