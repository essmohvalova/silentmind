package com.example.coursework_app.ui.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.UserRepository
import com.example.coursework_app.utils.onError
import com.example.coursework_app.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val uiStateFactory: OnboardingUiStateFactory,
    private val savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialState = uiStateFactory.create()
            _uiState.value = initialState
        }
    }

    fun updateNameAndEmail(name: String, email: String) {
        savedStateHandle["userName"] = name
        savedStateHandle["userEmail"] = email

        _uiState.update { currentState ->
            currentState.copy(
                dataState = currentState.dataState.copy(
                    userName = name,
                    userEmail = email
                )
            )
        }
    }

    fun selectCharacter(character: CharacterType) {
        savedStateHandle["selectedCharacter"] = character.name

        _uiState.update { currentState ->
            currentState.copy(
                dataState = currentState.dataState.copy(
                    selectedCharacter = character
                )
            )
        }
    }

    fun nextPage() {
        if (_uiState.value.currentPage < MAX_PAGE_COUNT) {
            val newPage = _uiState.value.currentPage + 1
            savedStateHandle["currentPage"] = newPage

            _uiState.update { currentState ->
                currentState.copy(currentPage = newPage)
            }
        }
    }

    fun previousPage() {
        if (_uiState.value.currentPage > 0) {
            val newPage = _uiState.value.currentPage - 1
            savedStateHandle["currentPage"] = newPage

            _uiState.update { currentState ->
                currentState.copy(currentPage = newPage)
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val dataState = _uiState.value.dataState

            val userId = dataState.userEmail.takeIf { it.isNotBlank() }
                ?: UUID.randomUUID().toString()

            runSuspendCatching {
                userRepository.saveUser(
                    User(
                        id = userId,
                        name = dataState.userName,
                        email = dataState.userEmail,
                        selectedCharacter = dataState.selectedCharacter,
                    )
                )
            }
                .onError { error ->
                    println("Error saving user: ${error.message}")
                }

            userPreferences.setUserId(userId)
            userPreferences.setOnboardingCompleted(true)

            _uiState.update { currentState ->
                currentState.copy(onboardingCompleted = true)
            }

            savedStateHandle.remove<String>("userName")
            savedStateHandle.remove<String>("userEmail")
            savedStateHandle.remove<String>("selectedCharacter")
            savedStateHandle.remove<Int>("currentPage")
        }
    }

    companion object {
        private const val MAX_PAGE_COUNT = 2
    }
}