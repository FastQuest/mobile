package com.example.fastquest.data.network.service

import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionOption
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for questions endpoints
 */
interface QuestionsApiService {
    
    /**
     * Get paginated list of questions
     * @param page Page number (default: 1)
     * @param perPage Items per page (default: 10)
     * @param orderBy Sort order (default: "created_at desc")
     * @return Paginated response with questions
     */
    @GET("questions")
    suspend fun getQuestions(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10,
        @Query("orderBy") orderBy: String = "created_at desc"
    ): PaginatedResponse<Question>
    
    /**
     * Get a specific question by ID
     * @param id Question ID
     * @return Question details with options
     */
    @GET("questions/{id}")
    suspend fun getQuestion(
        @Path("id") id: Int
    ): Question
    
    /**
     * Get all options for a specific question
     * @param questionId Question ID
     * @return List of question options
     */
    @GET("questions/{id}/question-options")
    suspend fun getQuestionOptions(
        @Path("id") questionId: Int
    ): List<QuestionOption>
}

// Made with Bob
