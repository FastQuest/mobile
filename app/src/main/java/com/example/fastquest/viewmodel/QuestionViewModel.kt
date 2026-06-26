package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.model.response.Answer
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionOption
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.data.repository.SubmissionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Question screen
 * Manages individual question display and answer submission
 */
class QuestionViewModel : ViewModel() {
    
    private val questionsRepository = QuestionsRepository(ApiClient.questionsService)
    private val submissionsRepository = SubmissionsRepository(
        ApiClient.submissionsService,
        ApiClient.answersService
    )
    
    // Current question state
    private val _question = MutableStateFlow<NetworkResult<Question>>(NetworkResult.Loading())
    val question: StateFlow<NetworkResult<Question>> = _question.asStateFlow()
    
    // Question options state
    private val _questionOptions = MutableStateFlow<NetworkResult<List<QuestionOption>>>(NetworkResult.Loading())
    val questionOptions: StateFlow<NetworkResult<List<QuestionOption>>> = _questionOptions.asStateFlow()
    
    // Selected answer
    private val _selectedOptionId = MutableStateFlow<Int?>(null)
    val selectedOptionId: StateFlow<Int?> = _selectedOptionId.asStateFlow()
    
    // User answers collection (for multiple questions)
    private val _userAnswers = MutableStateFlow<MutableList<Answer>>(mutableListOf())
    val userAnswers: StateFlow<List<Answer>> = _userAnswers.asStateFlow()
    
    // Submission state
    private val _submissionState = MutableStateFlow<NetworkResult<Boolean>?>(null)
    val submissionState: StateFlow<NetworkResult<Boolean>?> = _submissionState.asStateFlow()
    
    /**
     * Load question by ID
     */
    fun loadQuestion(questionId: Int) {
        viewModelScope.launch {
            _question.value = NetworkResult.Loading()
            _question.value = questionsRepository.getQuestion(questionId)
            
            // Also load question options
            loadQuestionOptions(questionId)
        }
    }
    
    /**
     * Load question options
     */
    fun loadQuestionOptions(questionId: Int) {
        viewModelScope.launch {
            _questionOptions.value = NetworkResult.Loading()
            _questionOptions.value = questionsRepository.getQuestionOptions(questionId)
        }
    }
    
    /**
     * Select an answer option
     */
    fun selectOption(optionId: Int) {
        _selectedOptionId.value = optionId
    }
    
    /**
     * Clear selected option
     */
    fun clearSelection() {
        _selectedOptionId.value = null
    }
    
    /**
     * Confirm answer for current question
     */
    fun confirmAnswer(questionId: Int) {
        val selectedId = _selectedOptionId.value
        if (selectedId != null) {
            val answer = Answer(
                questionId = questionId,
                selectedOptionId = selectedId,
                isCorrect = null // Will be determined by backend
            )
            
            // Add to answers collection
            val currentAnswers = _userAnswers.value.toMutableList()
            // Remove existing answer for this question if any
            currentAnswers.removeAll { it.questionId == questionId }
            currentAnswers.add(answer)
            _userAnswers.value = currentAnswers
            
            // Clear selection for next question
            clearSelection()
        }
    }
    
    /**
     * Submit all answers for a question set
     */
    fun submitAnswers(questionSetId: Int) {
        viewModelScope.launch {
            _submissionState.value = NetworkResult.Loading()
            
            val answers = _userAnswers.value
            if (answers.isEmpty()) {
                _submissionState.value = NetworkResult.Error("No answers to submit")
                return@launch
            }
            
            val result = submissionsRepository.createSubmission(questionSetId, answers)
            
            _submissionState.value = when (result) {
                is NetworkResult.Success -> NetworkResult.Success(true)
                is NetworkResult.Error -> NetworkResult.Error(result.message, result.code)
                is NetworkResult.Loading -> NetworkResult.Loading()
            }
        }
    }
    
    /**
     * Clear all user answers
     */
    fun clearAllAnswers() {
        _userAnswers.value = mutableListOf()
        _selectedOptionId.value = null
    }
    
    /**
     * Get answer for specific question
     */
    fun getAnswerForQuestion(questionId: Int): Answer? {
        return _userAnswers.value.find { it.questionId == questionId }
    }
    
    /**
     * Check if question has been answered
     */
    fun isQuestionAnswered(questionId: Int): Boolean {
        return _userAnswers.value.any { it.questionId == questionId }
    }
    
    /**
     * Clear submission state
     */
    fun clearSubmissionState() {
        _submissionState.value = null
    }
}

// Made with Bob
