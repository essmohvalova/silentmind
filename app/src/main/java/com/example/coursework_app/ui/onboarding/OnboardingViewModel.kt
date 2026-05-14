package com.example.coursework_app.ui.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.usecase.CompleteOnboardingUseCase
import com.example.coursework_app.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val uiStateFactory: OnboardingUiStateFactory,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val existingUser = getUserUseCase()
            val savedName = savedStateHandle.get<String>(KEY_USER_NAME).orEmpty()
            val savedEmail = savedStateHandle.get<String>(KEY_USER_EMAIL).orEmpty()
            val savedCharacter = parseSavedCharacter(savedStateHandle.get<String>(KEY_SELECTED_CHARACTER))
            val savedPage = savedStateHandle.get<Int>(KEY_CURRENT_PAGE) ?: 0

            _uiState.value = uiStateFactory.create(
                existingUser = existingUser,
                savedName = savedName,
                savedEmail = savedEmail,
                savedCharacter = savedCharacter,
                savedPage = savedPage,
            )
        }
    }

    fun updateNameAndEmail(name: String, email: String) {
        savedStateHandle[KEY_USER_NAME] = name
        savedStateHandle[KEY_USER_EMAIL] = email

        _uiState.update { currentState ->
            currentState.copy(
                contentData = currentState.contentData.copy(
                    userName = name,
                    userEmail = email,
                ),
            )
        }
    }

    fun selectCharacter(character: CharacterType) {
        savedStateHandle[KEY_SELECTED_CHARACTER] = character.name

        _uiState.update { currentState ->
            currentState.copy(
                contentData = currentState.contentData.copy(
                    selectedCharacter = character,
                ),
            )
        }
    }

    fun nextPage() {
        if (_uiState.value.currentPage < MAX_PAGE_COUNT) {
            val newPage = _uiState.value.currentPage + 1
            savedStateHandle[KEY_CURRENT_PAGE] = newPage

            _uiState.update { currentState ->
                currentState.copy(currentPage = newPage)
            }
        }
    }

    fun previousPage() {
        if (_uiState.value.currentPage > 0) {
            val newPage = _uiState.value.currentPage - 1
            savedStateHandle[KEY_CURRENT_PAGE] = newPage

            _uiState.update { currentState ->
                currentState.copy(currentPage = newPage)
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val dataState = _uiState.value.contentData

            completeOnboardingUseCase(
                userName = dataState.userName,
                userEmail = dataState.userEmail,
                selectedCharacter = dataState.selectedCharacter,
            )
                .onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(onboardingCompleted = true)
                    }

                    savedStateHandle.remove<String>(KEY_USER_NAME)
                    savedStateHandle.remove<String>(KEY_USER_EMAIL)
                    savedStateHandle.remove<String>(KEY_SELECTED_CHARACTER)
                    savedStateHandle.remove<Int>(KEY_CURRENT_PAGE)
                }
                .onFailure { error ->
                    println("Error saving user: ${error.message}")
                }
        }
    }

    companion object {
        private const val MAX_PAGE_COUNT = 2

        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_SELECTED_CHARACTER = "selectedCharacter"
        private const val KEY_CURRENT_PAGE = "currentPage"

        private fun parseSavedCharacter(raw: String?): CharacterType {
            if (raw.isNullOrBlank()) return CharacterType.CAT
            return runCatching { CharacterType.valueOf(raw) }.getOrDefault(CharacterType.CAT)
        }
    }
}
