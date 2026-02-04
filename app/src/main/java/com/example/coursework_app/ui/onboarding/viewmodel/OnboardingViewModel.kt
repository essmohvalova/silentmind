package com.example.coursework_app.ui.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.domain.model.User
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    sealed class OnboardingEvent {
        data class OnNameChanged(val name: String) : OnboardingEvent()
        data class OnCharacterSelected(val character: CharacterType) : OnboardingEvent()
        object OnCompleteClicked : OnboardingEvent()
        object OnSkipClicked : OnboardingEvent()
    }

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnNameChanged -> updateName(event.name)
            is OnboardingEvent.OnCharacterSelected -> updateCharacter(event.character)
            OnboardingEvent.OnCompleteClicked -> completeOnboarding()
            OnboardingEvent.OnSkipClicked -> skipOnboarding()
        }
    }



    private fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            isCompleteButtonEnabled = name.isNotBlank() && _uiState.value.selectedCharacter != null
        )
    }

    private fun updateCharacter(character: CharacterType) {
        _uiState.value = _uiState.value.copy(
            selectedCharacter = character,
            isCompleteButtonEnabled = _uiState.value.name.isNotBlank()
        )
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            try {
                val user = User(
                id = USER_ID,
                name = _uiState.value.name,
                selectedCharacter = _uiState.value.selectedCharacter ?: CharacterType.CAT,
                onboardingCompleted = true
            )
            withContext(Dispatchers.IO) {
                saveUserUseCase(user)
            }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun skipOnboarding() {
        viewModelScope.launch {
            try {
                val user = User(
                    id = USER_ID,
                    name = "",
                    selectedCharacter = CharacterType.CAT,
                    onboardingCompleted = true
                )
                withContext(Dispatchers.IO) {
                    saveUserUseCase(user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val USER_ID = 1
    }
}

data class OnboardingUiState(
    val name: String = "",
    val selectedCharacter: CharacterType? = null,
    val isCompleteButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)