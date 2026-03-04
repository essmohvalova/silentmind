package com.example.coursework_app.ui.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.domain.model.user.User
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // Данные пользователя
    private val userData = MutableStateFlow(
        UserData(
            id = UUID.randomUUID().toString(),
            name = "",
            email = "",
            selectedCharacter = CharacterType.CAT,
            onboardingCompleted = false
        )
    )

    init {
        checkIfOnboardingNeeded()
    }

    fun updateName(name: String) {
        userData.value = userData.value.copy(name = name)
    }

    fun updateEmail(email: String) {
        userData.value = userData.value.copy(email = email)
    }

    fun selectCharacter(character: CharacterType) {
        userData.value = userData.value.copy(selectedCharacter = character)
    }

    fun nextPage() {
        if (_currentPage.value < 2) {
            _currentPage.value += 1
        }
    }

    fun previousPage() {
        if (_currentPage.value > 0) {
            _currentPage.value -= 1
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val user = User(
                id = userData.value.id,
                name = userData.value.name,
                email = userData.value.email,
                selectedCharacter = userData.value.selectedCharacter,
                onboardingCompleted = true,
                createdAt = System.currentTimeMillis()
            )
            saveUserUseCase(user)
            _uiState.value = _uiState.value.copy(
                onboardingCompleted = true,
                user = user
            )
        }
    }

    private fun checkIfOnboardingNeeded() {
        viewModelScope.launch {
TODO()
            _uiState.value = _uiState.value.copy(
                isLoading = false
            )
        }
    }

    data class OnboardingUiState(
        val isLoading: Boolean = true,
        val onboardingCompleted: Boolean = false,
        val user: User? = null
    )

    data class UserData(
        val id: String,
        val name: String,
        val email: String,
        val selectedCharacter: CharacterType,
        val onboardingCompleted: Boolean
    )
}