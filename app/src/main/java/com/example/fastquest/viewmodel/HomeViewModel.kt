package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.ui.state.QuestionFilterSelection
import com.example.fastquest.ui.state.QuestionFiltersUiState
import com.example.fastquest.ui.state.QuestionSetsUiState
import com.example.fastquest.ui.state.QuestionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen
 * Manages question sets and questions lists
 */
class HomeViewModel(
    private val questionSetsRepository: QuestionSetsRepository,
    private val questionsRepository: QuestionsRepository
) : ViewModel() {

    // Question sets UI state
    private val _questionSetsState = MutableStateFlow(QuestionSetsUiState())
    val questionSetsState: StateFlow<QuestionSetsUiState> = _questionSetsState.asStateFlow()

    // Questions UI state
    private val _questionsState = MutableStateFlow(QuestionsUiState())
    val questionsState: StateFlow<QuestionsUiState> = _questionsState.asStateFlow()

    // Search term (pastas)
    private val _searchTerm = MutableStateFlow<String?>(null)
    val searchTerm: StateFlow<String?> = _searchTerm.asStateFlow()

    // Search term (perguntas)
    private val _questionsSearchTerm = MutableStateFlow<String?>(null)
    val questionsSearchTerm: StateFlow<String?> = _questionsSearchTerm.asStateFlow()

    // Available filter options (subjects, sources, years) for the Filtros dialog
    private val _questionFiltersState = MutableStateFlow(QuestionFiltersUiState())
    val questionFiltersState: StateFlow<QuestionFiltersUiState> = _questionFiltersState.asStateFlow()

    // Currently selected filters for questions
    private val _selectedFilters = MutableStateFlow(QuestionFilterSelection())
    val selectedFilters: StateFlow<QuestionFilterSelection> = _selectedFilters.asStateFlow()

    // Sort order for question sets (pastas)
    private val _questionSetsOrderBy = MutableStateFlow("created_at desc")
    val questionSetsOrderBy: StateFlow<String> = _questionSetsOrderBy.asStateFlow()

    /**
     * Load question sets with pagination
     */
    fun loadQuestionSets(page: Int = 1, searchTerm: String? = null, refresh: Boolean = false) {
        viewModelScope.launch {
            _questionSetsState.value = _questionSetsState.value.copy(isLoading = true, error = null)

            val result = questionSetsRepository.getQuestionSets(
                page = page,
                perPage = 10,
                orderBy = _questionSetsOrderBy.value,
                searchTerm = searchTerm,
                includeRelations = true
            )

            when (result) {
                is NetworkResult.Success -> {
                    _questionSetsState.value = _questionSetsState.value.copy(
                        questionSets = result.data.data,
                        isLoading = false,
                        currentPage = result.data.pagination.currentPage,
                        totalPages = result.data.pagination.totalPages,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _questionSetsState.value = _questionSetsState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _questionSetsState.value = _questionSetsState.value.copy(isLoading = true)
                }
            }
        }
    }

    /**
     * Load questions with pagination, applying the current search term and selected filters
     */
    fun loadQuestions(page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch {
            _questionsState.value = _questionsState.value.copy(isLoading = true, error = null)

            val filters = _selectedFilters.value
            val result = questionsRepository.getQuestions(
                page = page,
                perPage = 10,
                orderBy = filters.orderBy,
                searchTerm = _questionsSearchTerm.value?.takeIf { it.isNotBlank() }
                    ?: filters.topics.lastOrNull(),
                subjectId = filters.subjectId,
                sourceId = filters.sourceId,
                year = filters.year
            )

            when (result) {
                is NetworkResult.Success -> {
                    _questionsState.value = _questionsState.value.copy(
                        questions = result.data.data,
                        isLoading = false,
                        currentPage = result.data.pagination.currentPage,
                        totalPages = result.data.pagination.totalPages,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _questionsState.value = _questionsState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _questionsState.value = _questionsState.value.copy(isLoading = true)
                }
            }
        }
    }

    /**
     * Load the available filter options (subjects, sources, years) for the Filtros dialog
     */
    fun loadQuestionFilters() {
        viewModelScope.launch {
            _questionFiltersState.value = _questionFiltersState.value.copy(isLoading = true, error = null)

            when (val result = questionsRepository.getQuestionFilters()) {
                is NetworkResult.Success -> {
                    _questionFiltersState.value = _questionFiltersState.value.copy(
                        filters = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _questionFiltersState.value = _questionFiltersState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _questionFiltersState.value = _questionFiltersState.value.copy(isLoading = true)
                }
            }
        }
    }

    /**
     * Apply a new filter selection and reload questions from page 1
     */
    fun applyFilters(selection: QuestionFilterSelection) {
        _selectedFilters.value = selection
        loadQuestions(page = 1, refresh = true)
    }

    /**
     * Reset all selected filters and reload questions from page 1
     */
    fun resetFilters() {
        _selectedFilters.value = QuestionFilterSelection()
        loadQuestions(page = 1, refresh = true)
    }

    /**
     * Apply a new sort order for question sets (pastas) and reload from page 1
     */
    fun applyQuestionSetsOrderBy(orderBy: String) {
        _questionSetsOrderBy.value = orderBy
        loadQuestionSets(page = 1, searchTerm = _searchTerm.value, refresh = true)
    }

    /**
     * Search questions by statement text
     */
    fun searchQuestions(term: String) {
        _questionsSearchTerm.value = term
        loadQuestions(page = 1, refresh = true)
    }

    /**
     * Search question sets
     */
    fun searchQuestionSets(term: String) {
        _searchTerm.value = term
        loadQuestionSets(page = 1, searchTerm = term, refresh = true)
    }

    /**
     * Clear search (pastas or perguntas depending on current mode)
     */
    fun clearSearch(isSearchingFolders: Boolean = true) {
        if (isSearchingFolders) {
            _searchTerm.value = null
            loadQuestionSets(page = 1, searchTerm = null, refresh = true)
        } else {
            _questionsSearchTerm.value = null
            loadQuestions(page = 1, refresh = true)
        }
    }
    
    /**
     * Go to next page of question sets
     */
    fun nextQuestionSetsPage() {
        val currentPage = _questionSetsState.value.currentPage
        val totalPages = _questionSetsState.value.totalPages
        if (currentPage < totalPages) {
            loadQuestionSets(page = currentPage + 1, searchTerm = _searchTerm.value)
        }
    }
    
    /**
     * Go to previous page of question sets
     */
    fun previousQuestionSetsPage() {
        val currentPage = _questionSetsState.value.currentPage
        if (currentPage > 1) {
            loadQuestionSets(page = currentPage - 1, searchTerm = _searchTerm.value)
        }
    }
    
    /**
     * Go to specific page of question sets
     */
    fun goToQuestionSetsPage(page: Int) {
        if (page in 1.._questionSetsState.value.totalPages) {
            loadQuestionSets(page = page, searchTerm = _searchTerm.value)
        }
    }
    
    /**
     * Go to next page of questions
     */
    fun nextQuestionsPage() {
        val currentPage = _questionsState.value.currentPage
        val totalPages = _questionsState.value.totalPages
        if (currentPage < totalPages) {
            loadQuestions(page = currentPage + 1)
        }
    }
    
    /**
     * Go to previous page of questions
     */
    fun previousQuestionsPage() {
        val currentPage = _questionsState.value.currentPage
        if (currentPage > 1) {
            loadQuestions(page = currentPage - 1)
        }
    }
    
    /**
     * Go to specific page of questions
     */
    fun goToQuestionsPage(page: Int) {
        if (page in 1.._questionsState.value.totalPages) {
            loadQuestions(page = page)
        }
    }
    
    /**
     * Refresh current data
     */
    fun refresh(isSearchingFolders: Boolean) {
        if (isSearchingFolders) {
            loadQuestionSets(
                page = _questionSetsState.value.currentPage,
                searchTerm = _searchTerm.value,
                refresh = true
            )
        } else {
            loadQuestions(page = _questionsState.value.currentPage, refresh = true)
        }
    }
}