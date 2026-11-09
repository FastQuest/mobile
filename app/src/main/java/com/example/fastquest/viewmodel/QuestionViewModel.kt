package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.model.response.Answer
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.data.repository.SubmissionsRepository
import com.example.fastquest.ui.state.QuestionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Question screen
 * Manages individual question display and answer submission
 */
class QuestionViewModel(
    private val questionsRepository: QuestionsRepository,
    private val submissionsRepository: SubmissionsRepository
) : ViewModel() {
    
    // Question UI state
    private val _questionState = MutableStateFlow(QuestionUiState())
    val questionState: StateFlow<QuestionUiState> = _questionState.asStateFlow()
    
    // Selected answer option ID
    private val _selectedOptionId = MutableStateFlow<String?>(null)
    val selectedOptionId: StateFlow<String?> = _selectedOptionId.asStateFlow()
    
    // Timer state
    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()
    
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()
    
    /**
     * Load question by ID
     */
    fun loadQuestion(questionId: String) {
        viewModelScope.launch {
            _questionState.value = QuestionUiState(isLoading = true)
            _selectedOptionId.value = null
            
            // Convert String ID to Int for API call
            val id = questionId.toIntOrNull() ?: run {
                _questionState.value = _questionState.value.copy(
                    isLoading = false,
                    error = "Invalid question ID"
                )
                return@launch
            }
            
            when (val result = questionsRepository.getQuestion(id)) {
                is NetworkResult.Success -> {
                    // Load options
                    when (val optionsResult = questionsRepository.getQuestionOptions(id)) {
                        is NetworkResult.Success -> {
                            _questionState.value = _questionState.value.copy(
                                question = result.data,
                                options = optionsResult.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is NetworkResult.Error -> {
                            _questionState.value = _questionState.value.copy(
                                question = result.data,
                                isLoading = false,
                                error = optionsResult.message
                            )
                        }
                        is NetworkResult.Loading -> {
                            _questionState.value = _questionState.value.copy(isLoading = true)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _questionState.value = _questionState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _questionState.value = _questionState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    /**
     * Select an answer option
     */
    fun selectOption(optionId: String) {
        _selectedOptionId.value = optionId
        _questionState.value = _questionState.value.copy(submissionError = null)
    }
    
    /**
     * Submit answer
     */
    fun submitAnswer(questionSetId: Int?) {
        if (_questionState.value.isSubmitting || _questionState.value.answerSubmitted) return
        val question = _questionState.value.question ?: return
        val selectedOptionId = _selectedOptionId.value?.toIntOrNull() ?: return

        if (questionSetId == null) {
            _questionState.value = _questionState.value.copy(
                submissionError = "Para registrar a resposta, abra a questão por uma lista."
            )
            return
        }

        viewModelScope.launch {
            _questionState.value = _questionState.value.copy(
                isSubmitting = true,
                answerSubmitted = false,
                submissionError = null
            )

            when (val result = submissionsRepository.createSubmission(
                questionSetId = questionSetId,
                answers = listOf(Answer(questionId = question.id, selectedOptionId = selectedOptionId))
            )) {
                is NetworkResult.Success -> {
                    _questionState.value = _questionState.value.copy(
                        isSubmitting = false,
                        answerSubmitted = true
                    )
                }
                is NetworkResult.Error -> {
                    _questionState.value = _questionState.value.copy(
                        isSubmitting = false,
                        submissionError = result.message
                    )
                }
                is NetworkResult.Loading -> {
                    _questionState.value = _questionState.value.copy(isSubmitting = true)
                }
            }
        }
    }
    
    /**
     * Start timer
     */
    fun startTimer() {
        _isTimerRunning.value = true
    }
    
    /**
     * Pause timer
     */
    fun pauseTimer() {
        _isTimerRunning.value = false
    }
    
    /**
     * Reset timer
     */
    fun resetTimer() {
        _timerSeconds.value = 0
        _isTimerRunning.value = false
    }
    
    /**
     * Increment timer (called every second)
     */
    fun incrementTimer() {
        if (_isTimerRunning.value) {
            _timerSeconds.value += 1
        }
    }
    
    /**
     * Clear selection
     */
    fun clearSelection() {
        _selectedOptionId.value = null
    }
}