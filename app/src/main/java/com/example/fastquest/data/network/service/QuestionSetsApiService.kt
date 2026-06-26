package com.example.fastquest.data.network.service

import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionSet
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for question sets endpoints
 */
interface QuestionSetsApiService {
    
    /**
     * Get paginated list of question sets with optional filters
     * @param page Page number (default: 1)
     * @param perPage Items per page (default: 10)
     * @param orderBy Sort order (default: "created_at desc")
     * @param userId Filter by creator user ID
     * @param isPrivate Filter by visibility
     * @param searchTerm Search in name or description
     * @param include Relationships to include (comma-separated: "user,questions")
     * @return Paginated response with question sets
     */
    @GET("question-sets")
    suspend fun getQuestionSets(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10,
        @Query("orderBy") orderBy: String = "created_at desc",
        @Query("userId") userId: Int? = null,
        @Query("isPrivate") isPrivate: Boolean? = null,
        @Query("statement") searchTerm: String? = null,
        @Query("include") include: String? = null
    ): PaginatedResponse<QuestionSet>
    
    /**
     * Get a specific question set by ID
     * @param id Question set ID
     * @param include Relationships to include (comma-separated: "user,questions")
     * @return Question set details
     */
    @GET("question-sets/{id}")
    suspend fun getQuestionSet(
        @Path("id") id: Int,
        @Query("include") include: String? = "user,questions"
    ): QuestionSet
    
    /**
     * Get all questions in a specific question set
     * @param id Question set ID
     * @return List of questions
     */
    @GET("question-sets/{id}/questions")
    suspend fun getQuestionSetQuestions(
        @Path("id") id: Int
    ): List<Question>
}

// Made with Bob
