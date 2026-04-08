package com.example.coursework_app.ui.onboarding

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import com.example.coursework_app.utils.onError
import com.example.coursework_app.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
) : ViewModel() {

    private val _dataState: MutableState<OnboardingDataState> = mutableStateOf(OnboardingDataState())

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateNameAndEmail(name: String, email: String) {
        _dataState.value = _dataState.value.copy(userName = name, userEmail = email)
    }

    fun selectCharacter(character: CharacterType) {
        _dataState.value = _dataState.value.copy(selectedCharacter = character)
        _uiState.value = _uiState.value.copy(selectedCharacter = character)
    }

    fun nextPage() {
        if (_uiState.value.currentPage < MAX_PAGE_COUNT) {
            _uiState.value = _uiState.value.copy(currentPage = _uiState.value.currentPage + 1)
        }
    }

    fun previousPage() {
        if (_uiState.value.currentPage <= MAX_PAGE_COUNT) {
            _uiState.value = _uiState.value.copy(currentPage = _uiState.value.currentPage - 1)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            runSuspendCatching {
                saveUserUseCase(
                    User(
                        id = _dataState.value.userEmail,
                        name = _dataState.value.userName,
                        email = _dataState.value.userEmail,
                        selectedCharacter = _dataState.value.selectedCharacter,
                    )
                )
            }
                .onSuccess { _uiState.value = _uiState.value.copy(onboardingCompleted = true) }
                .onError { /*TODO() handle error }*/ }
        }
    }

    private data class OnboardingDataState(
        val userName: String = "Гость",
        val userEmail: String = "",
        val selectedCharacter: CharacterType = CharacterType.CAT,
    )

    private companion object {

        const val MAX_PAGE_COUNT = 2
    }
}
