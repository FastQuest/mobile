package com.example.fastquest.di

import android.content.Context
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.repository.AuthRepository
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.data.repository.SubmissionsRepository
import com.example.fastquest.viewmodel.AuthViewModelFactory
import com.example.fastquest.viewmodel.FolderDetailsViewModelFactory
import com.example.fastquest.viewmodel.HomeViewModelFactory
import com.example.fastquest.viewmodel.QuestionViewModelFactory
import com.example.fastquest.viewmodel.ResultsViewModelFactory

class AppContainer(context: Context) {
    private val apiClient = ApiClient(context.applicationContext)

    val tokenManager = apiClient.tokenManager
    val authRepository = AuthRepository(apiClient.authService, apiClient.tokenManager)
    val questionSetsRepository = QuestionSetsRepository(apiClient.questionSetsService)
    val questionsRepository = QuestionsRepository(apiClient.questionsService)
    val submissionsRepository = SubmissionsRepository(
        apiClient.submissionsService,
        apiClient.answersService
    )

    val authViewModelFactory = AuthViewModelFactory(authRepository)
    val homeViewModelFactory = HomeViewModelFactory(questionSetsRepository, questionsRepository)
    val questionViewModelFactory = QuestionViewModelFactory(questionsRepository, submissionsRepository)
    val resultsViewModelFactory = ResultsViewModelFactory(submissionsRepository)
    val folderDetailsViewModelFactory = FolderDetailsViewModelFactory(questionSetsRepository)
}