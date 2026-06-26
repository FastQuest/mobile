package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionSet
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen
 * Manages question sets and questions lists
 */
class HomeViewModel : ViewModel() {
    
    private val questionSetsRepository = QuestionSetsRepository(ApiClient.questionSetsService)
    private val questionsRepository = QuestionsRepository(ApiClient.questionsService)
    
    // Question sets state
    private val _questionSets = MutableStateFlow<NetworkResult<List<QuestionSet>>>(NetworkResult.Loading())
    val questionSets: StateFlow<NetworkResult<List<QuestionSet>>> = _questionSets.asStateFlow()
    
    // Questions state
    private val _questions = MutableStateFlow<NetworkResult<List<Question>>>(NetworkResult.Loading())
    val questions: StateFlow<NetworkResult<List<Question>>> = _questions.asStateFlow()
    
    // Current page tracking
    private val _currentQuestionSetsPage = MutableStateFlow(1)
    val currentQuestionSetsPage: StateFlow<Int> = _currentQuestionSetsPage.asStateFlow()
    
    private val _currentQuestionsPage = MutableStateFlow(1)
    val currentQuestionsPage: StateFlow<Int> = _currentQuestionsPage.asStateFlow()
    
    // Total pages (from pagination)
    private val _totalQuestionSetsPages = MutableStateFlow(1)
    val totalQuestionSetsPages: StateFlow<Int> = _totalQuestionSetsPages.asStateFlow()
    
    private val _totalQuestionsPages = MutableStateFlow(1)
    val totalQuestionsPages: StateFlow<Int> = _totalQuestionsPages.asStateFlow()
    
    // Search term
    private val _searchTerm = MutableStateFlow<String?>(null)
    val searchTerm: StateFlow<String?> = _searchTerm.asStateFlow()
    
    /**
     * Load question sets with pagination
     */
    fun loadQuestionSets(page: Int = 1, searchTerm: String? = null, refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh || _questionSets.value !is NetworkResult.Success) {
                _questionSets.value = NetworkResult.Loading()
            }
            
            val result = questionSetsRepository.getQuestionSets(
                page = page,
                perPage = 10,
                searchTerm = searchTerm,
                includeRelations = true
            )
            
            when (result) {
                is NetworkResult.Success -> {
                    _questionSets.value = NetworkResult.Success(result.data.data)
                    _currentQuestionSetsPage.value = result.data.pagination.currentPage
                    _totalQuestionSetsPages.value = result.data.pagination.totalPages
                }
                is NetworkResult.Error -> {
                    _questionSets.value = result
                }
                is NetworkResult.Loading -> {
                    _questionSets.value = result
                }
            }
        }
    }
    
    /**
     * Load questions with pagination
     */
    fun loadQuestions(page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh || _questions.value !is NetworkResult.Success) {
                _questions.value = NetworkResult.Loading()
            }
            
            val result = questionsRepository.getQuestions(page, perPage = 10)
            
            when (result) {
                is NetworkResult.Success -> {
                    _questions.value = NetworkResult.Success(result.data.data)
                    _currentQuestionsPage.value = result.data.pagination.currentPage
                    _totalQuestionsPages.value = result.data.pagination.totalPages
                }
                is NetworkResult.Error -> {
                    _questions.value = result
                }
                is NetworkResult.Loading -> {
                    _questions.value = result
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
        val currentPage = _currentQuestionSetsPage.value
        val totalPages = _totalQuestionSetsPages.value
        if (currentPage < totalPages) {
            loadQuestionSets(page = currentPage + 1, searchTerm = _searchTerm.value)
        }
    }
    
    /**
     * Go to previous page of question sets
     */
    fun previousQuestionSetsPage() {
        val currentPage = _currentQuestionSetsPage.value
        if (currentPage > 1) {
            loadQuestionSets(page = currentPage - 1, searchTerm = _searchTerm.value)
        }
    }
    
    /**
     * Go to specific page of question sets
     */
    fun goToQuestionSetsPage(page: Int) {
        if (page in 1.._totalQuestionSetsPages.value) {
            loadQuestionSets(page = page, searchTerm = _searchTerm.value)
        }
    }
    
    /**
     * Go to next page of questions
     */
    fun nextQuestionsPage() {
        val currentPage = _currentQuestionsPage.value
        val totalPages = _totalQuestionsPages.value
        if (currentPage < totalPages) {
            loadQuestions(page = currentPage + 1)
        }
    }
    
    /**
     * Go to previous page of questions
     */
    fun previousQuestionsPage() {
        val currentPage = _currentQuestionsPage.value
        if (currentPage > 1) {
            loadQuestions(page = currentPage - 1)
        }
    }
    
    /**
     * Go to specific page of questions
     */
    fun goToQuestionsPage(page: Int) {
        if (page in 1.._totalQuestionsPages.value) {
            loadQuestions(page = page)
        }
    }
    
    /**
     * Refresh current data
     */
    fun refresh(isSearchingFolders: Boolean) {
        if (isSearchingFolders) {
            loadQuestionSets(
                page = _currentQuestionSetsPage.value,
                searchTerm = _searchTerm.value,
                refresh = true
            )
        } else {
            loadQuestions(page = _currentQuestionsPage.value, refresh = true)
        }
    }
}

// Made with Bob
