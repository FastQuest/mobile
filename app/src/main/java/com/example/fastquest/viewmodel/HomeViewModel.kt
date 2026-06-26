package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.data.repository.QuestionsRepository
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
    
    // Search term
    private val _searchTerm = MutableStateFlow<String?>(null)
    val searchTerm: StateFlow<String?> = _searchTerm.asStateFlow()
    
    /**
     * Load question sets with pagination
     */
    fun loadQuestionSets(page: Int = 1, searchTerm: String? = null, refresh: Boolean = false) {
        viewModelScope.launch {
            _questionSetsState.value = _questionSetsState.value.copy(isLoading = true, error = null)
            
            val result = questionSetsRepository.getQuestionSets(
                page = page,
                perPage = 10,
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
     * Load questions with pagination
     */
    fun loadQuestions(page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch {
            _questionsState.value = _questionsState.value.copy(isLoading = true, error = null)
            
            val result = questionsRepository.getQuestions(page, perPage = 10)
            
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
     * Search question sets
     */
    fun searchQuestionSets(term: String) {
        _searchTerm.value = term
        loadQuestionSets(page = 1, searchTerm = term, refresh = true)
    }
    
    /**
     * Clear search
     */
    fun clearSearch() {
        _searchTerm.value = null
        loadQuestionSets(page = 1, searchTerm = null, refresh = true)
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

// Made with Bob
