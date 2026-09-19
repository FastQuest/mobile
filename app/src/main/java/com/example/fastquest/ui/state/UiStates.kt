package com.example.fastquest.ui.state

import com.example.fastquest.data.model.response.*

/**
 * UI State for Authentication screens
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

/**
 * UI State for Home screen - Question Sets
 */
data class QuestionSetsUiState(
    val questionSets: List<QuestionSet> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1
)

/**
 * UI State for Home screen - Questions
 */
data class QuestionsUiState(
    val questions: List<Question> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1
)

/**
 * Selected filter values for the questions search (Home screen "Filtros" dialog)
 */
data class QuestionFilterSelection(
    val orderBy: String = "created_at desc",
    val subjectId: Int? = null,
    val subjectName: String? = null,
    val sourceId: Int? = null,
    val sourceName: String? = null,
    val year: Int? = null,
    val topics: List<String> = emptyList()
) {
    val isEmpty: Boolean
        get() = orderBy == "created_at desc" && subjectId == null && sourceId == null &&
            year == null && topics.isEmpty()
}

/**
 * UI State for available filter options fetched from GET /questions/filters
 */
data class QuestionFiltersUiState(
    val filters: QuestionFilters = QuestionFilters(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * UI State for Question screen
 */
data class QuestionUiState(
    val question: Question? = null,
    val options: List<QuestionOption> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSubmitting: Boolean = false,
    val answerSubmitted: Boolean = false
)

/**
 * UI State for Results/Performance screen
 */
data class PerformanceUiState(
    val performance: PerformanceMetrics? = null,
    val submissions: List<Submission> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
