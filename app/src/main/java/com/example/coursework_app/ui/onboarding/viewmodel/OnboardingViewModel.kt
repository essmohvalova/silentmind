package com.example.coursework_app.ui.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCase

import com.example.coursework_app.ui.onboarding.OnboardingEvent
import com.example.coursework_app.ui.onboarding.OnboardingUiState
import com.example.coursework_app.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    init {
        checkExistingUser()
    }

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnNameChanged -> updateName(event.name)
            is OnboardingEvent.OnCharacterSelected -> updateCharacter(event.character)
            OnboardingEvent.OnCompleteClicked -> completeOnboarding()
            OnboardingEvent.OnSkipClicked -> skipOnboarding()
        }
    }

    private fun updateName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                isCompleteButtonEnabled = currentState.isFormValid().also {
                }
            )
        }
        updateButtonState()
    }

    private fun updateCharacter(character: CharacterType) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCharacter = character
            )
        }
        updateButtonState()
    }

    private fun updateButtonState() {
        _uiState.update { currentState ->
            currentState.copy(
                isCompleteButtonEnabled = currentState.isFormValid()
            )
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = runSuspendCatching {
                val user = User(
                    id = TODO(),
                    name = _uiState.value.name,
                    email = "",
                    selectedCharacter = _uiState.value.selectedCharacter ?: CharacterType.CAT,
                    onboardingCompleted = true,
                    createdAt = System.currentTimeMillis()
                )
                saveUserUseCase(user)
            }

            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isSuccess = true,
                        error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = error.message ?: "Не удалось сохранить пользователя"
                    )
                }
            }
        }
    }

    private fun skipOnboarding() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = runSuspendCatching {
                val user = User(
                    id = TODO("waiting for room"),
                    name = "", // Пустое имя при пропуске
                    email = "", // Пустой email при пропуске
                    selectedCharacter = CharacterType.CAT, // Дефолтный персонаж
                    onboardingCompleted = true,
                    createdAt = System.currentTimeMillis()
                )
                saveUserUseCase(user)
            }

            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isSuccess = true,
                        error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = error.message ?: "Не удалось сохранить пользователя"
                    )
                }
            }
        }
    }

    private fun checkExistingUser() {
        viewModelScope.launch {
            val result = runSuspendCatching {
                getUserUseCase()
            }

            result.onSuccess { user ->
                user?.let {
                    _uiState.update { state ->
                        state.copy(
                            name = it.name,
                            selectedCharacter = it.selectedCharacter,
                            isCompleteButtonEnabled = it.name.isNotBlank() && it.selectedCharacter != null
                        )
                    }
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        error = "Не удалось загрузить данные пользователя: ${error.message}"
                    )
                }
            }
        }
    }
    }