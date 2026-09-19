package com.example.fastquest.data.network.service

import com.example.fastquest.data.model.response.PaginatedResponse
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionFilters
import com.example.fastquest.data.model.response.QuestionOption
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for questions endpoints
 */
interface QuestionsApiService {

    /**
     * Get paginated list of questions with optional filters
     * @param page Page number (default: 1)
     * @param perPage Items per page (default: 10)
     * @param orderBy Sort order (default: "created_at desc")
     * @param include Relationships to include (comma-separated: "question-options,user,subject,source")
     * @param statement Search term matched against the question statement (backend: "statement" ILIKE filter)
     * @param subject Filter by subject ID (backend: "subject" filter)
     * @param source Filter by source ID (backend: "source" filter, matches source_exam_instance.source_id)
     * @param year Filter by exam/creation year (backend: "year" filter)
     * @param questionOption Search term matched against question option text (backend: "question_option" filter)
     * @return Paginated response with questions
     */
    @GET("questions")
    suspend fun getQuestions(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10,
        @Query("orderBy") orderBy: String = "created_at desc",
        @Query("include") include: String? = null,
        @Query("statement") statement: String? = null,
        @Query("subject") subject: Int? = null,
        @Query("source") source: Int? = null,
        @Query("year") year: Int? = null,
        @Query("question_option") questionOption: String? = null
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

    /**
     * Get available filter options (subjects, sources, years) for questions
     * @return Available filter values
     */
    @GET("questions/filters")
    suspend fun getQuestionFilters(): QuestionFilters
}
