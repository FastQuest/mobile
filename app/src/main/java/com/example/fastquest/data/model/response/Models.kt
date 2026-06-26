package com.example.fastquest.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * User model
 */
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "name")
    val name: String,
    
    @Json(name = "email")
    val email: String,
    
    @Json(name = "created_at")
    val createdAt: String? = null
)

/**
 * Subject model
 */
@JsonClass(generateAdapter = true)
data class Subject(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "name")
    val name: String
)

/**
 * Source model
 */
@JsonClass(generateAdapter = true)
data class Source(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "name")
    val name: String,
    
    @Json(name = "type")
    val type: String? = null,
    
    @Json(name = "year")
    val year: Int? = null,
    
    @Json(name = "edition")
    val edition: Int? = null,
    
    @Json(name = "phase")
    val phase: Int? = null
)

/**
 * Question Option model
 */
@JsonClass(generateAdapter = true)
data class QuestionOption(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "question_id")
    val questionId: Int,
    
    @Json(name = "text")
    val text: String,
    
    @Json(name = "is_correct")
    val isCorrect: Boolean
)

/**
 * Question model
 */
@JsonClass(generateAdapter = true)
data class Question(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "statement")
    val statement: String,
    
    @Json(name = "subject")
    val subject: Subject? = null,
    
    @Json(name = "source")
    val source: Source? = null,
    
    @Json(name = "question_options")
    val questionOptions: List<QuestionOption>? = null,
    
    @Json(name = "user")
    val user: User? = null,
    
    @Json(name = "created_at")
    val createdAt: String,
    
    @Json(name = "updated_at")
    val updatedAt: String
)

/**
 * Question Set model
 */
@JsonClass(generateAdapter = true)
data class QuestionSet(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "name")
    val name: String,
    
    @Json(name = "description")
    val description: String,
    
    @Json(name = "type")
    val type: String,
    
    @Json(name = "is_private")
    val isPrivate: Boolean,
    
    @Json(name = "created_at")
    val createdAt: String,
    
    @Json(name = "user")
    val user: User? = null,
    
    @Json(name = "questions")
    val questions: List<Question>? = null
)

/**
 * Pagination metadata
 */
@JsonClass(generateAdapter = true)
data class Pagination(
    @Json(name = "current_page")
    val currentPage: Int,
    
    @Json(name = "per_page")
    val perPage: Int,
    
    @Json(name = "total")
    val total: Int,
    
    @Json(name = "total_pages")
    val totalPages: Int
)

/**
 * Paginated response wrapper
 */
@JsonClass(generateAdapter = true)
data class PaginatedResponse<T>(
    @Json(name = "data")
    val data: List<T>,
    
    @Json(name = "pagination")
    val pagination: Pagination
)

/**
 * Answer model for submissions
 */
@JsonClass(generateAdapter = true)
data class Answer(
    @Json(name = "question_id")
    val questionId: Int,
    
    @Json(name = "selected_option_id")
    val selectedOptionId: Int,
    
    @Json(name = "is_correct")
    val isCorrect: Boolean? = null
)

/**
 * Submission model
 */
@JsonClass(generateAdapter = true)
data class Submission(
    @Json(name = "id")
    val id: Int,
    
    @Json(name = "question_set_id")
    val questionSetId: Int,
    
    @Json(name = "user_id")
    val userId: Int,
    
    @Json(name = "answers")
    val answers: List<Answer>,
    
    @Json(name = "score")
    val score: Float? = null,
    
    @Json(name = "created_at")
    val createdAt: String
)

/**
 * Performance by subject
 */
@JsonClass(generateAdapter = true)
data class SubjectPerformance(
    @Json(name = "subject")
    val subject: String,
    
    @Json(name = "total")
    val total: Int,
    
    @Json(name = "correct")
    val correct: Int,
    
    @Json(name = "accuracy")
    val accuracy: Float
)

/**
 * User performance metrics
 */
@JsonClass(generateAdapter = true)
data class PerformanceMetrics(
    @Json(name = "total_questions_answered")
    val totalQuestionsAnswered: Int,
    
    @Json(name = "correct_answers")
    val correctAnswers: Int,
    
    @Json(name = "accuracy")
    val accuracy: Float,
    
    @Json(name = "by_subject")
    val bySubject: List<SubjectPerformance>? = null
)

/**
 * Overall performance metrics
 */
@JsonClass(generateAdapter = true)
data class OverallPerformance(
    @Json(name = "total_submissions")
    val totalSubmissions: Int,
    
    @Json(name = "average_score")
    val averageScore: Float,
    
    @Json(name = "best_score")
    val bestScore: Float,
    
    @Json(name = "recent_trend")
    val recentTrend: String? = null
)

// Made with Bob
