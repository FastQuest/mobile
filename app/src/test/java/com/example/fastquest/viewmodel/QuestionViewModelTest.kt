package com.example.fastquest.viewmodel

import com.example.fastquest.data.model.response.Answer
import com.example.fastquest.data.model.response.Question
import com.example.fastquest.data.model.response.QuestionOption
import com.example.fastquest.data.model.response.Submission
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.data.repository.SubmissionsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionViewModelTest {
    @Test
    fun `submits selected answer with question and question set IDs`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val questionsRepository = mockk<QuestionsRepository>()
            val submissionsRepository = mockk<SubmissionsRepository>()
            val question = mockk<Question>()
            every { question.id } returns 41
            val answer = Answer(questionId = 41, selectedOptionId = 17)
            val submission = Submission(
                id = 1,
                questionSetId = 9,
                userId = 2,
                answers = listOf(answer),
                score = 1f,
                createdAt = "2026-11-09"
            )

            coEvery { questionsRepository.getQuestion(41) } returns NetworkResult.Success(question)
            coEvery { questionsRepository.getQuestionOptions(41) } returns
                NetworkResult.Success(emptyList<QuestionOption>())
            coEvery { submissionsRepository.createSubmission(9, listOf(answer)) } returns
                NetworkResult.Success(submission)

            val viewModel = QuestionViewModel(questionsRepository, submissionsRepository)
            viewModel.loadQuestion("41")
            advanceUntilIdle()
            viewModel.selectOption("17")
            viewModel.submitAnswer(questionSetId = 9)
            advanceUntilIdle()

            assertTrue(viewModel.questionState.value.answerSubmitted)
            coVerify(exactly = 1) {
                submissionsRepository.createSubmission(9, listOf(answer))
            }
        } finally {
            Dispatchers.resetMain()
        }
    }
}