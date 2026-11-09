package com.example.fastquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fastquest.data.network.NetworkResult
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.ui.state.FolderDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FolderDetailsViewModel(
    private val questionSetsRepository: QuestionSetsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FolderDetailsUiState())
    val state: StateFlow<FolderDetailsUiState> = _state.asStateFlow()

    fun loadFolder(folderId: String) {
        val id = folderId.toIntOrNull()
        if (id == null) {
            _state.value = FolderDetailsUiState(error = "Identificador de lista inválido.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = questionSetsRepository.getQuestionSet(id, includeRelations = true)) {
                is NetworkResult.Success -> _state.value = FolderDetailsUiState(questionSet = result.data)
                is NetworkResult.Error -> _state.value = FolderDetailsUiState(error = result.message)
                is NetworkResult.Loading -> _state.value = _state.value.copy(isLoading = true)
            }
        }
    }
}