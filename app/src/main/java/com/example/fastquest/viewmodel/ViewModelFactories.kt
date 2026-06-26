package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fastquest.data.repository.AuthRepository
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.data.repository.SubmissionsRepository

/**
 * Factory for AuthViewModel
 */
class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for HomeViewModel
 */
class HomeViewModelFactory(
    private val questionSetsRepository: QuestionSetsRepository,
    private val questionsRepository: QuestionsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(questionSetsRepository, questionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for QuestionViewModel
 */
class QuestionViewModelFactory(
    private val questionsRepository: QuestionsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(questionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for ResultsViewModel
 */
class ResultsViewModelFactory(
    private val submissionsRepository: SubmissionsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            return ResultsViewModel(submissionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Made with Bob