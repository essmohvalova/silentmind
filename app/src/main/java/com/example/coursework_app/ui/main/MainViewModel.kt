package com.example.coursework_app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.ObserveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val observeUserUseCase: ObserveUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            observeUserUseCase("current_user_id").collect { user ->
                _uiState.value = _uiState.value.copy(
                    userName = user?.name ?: "Гость"
                )
            }
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            val user = getUserUseCase("current_user_id")
            _uiState.value = _uiState.value.copy(
                userName = user?.name ?: "Гость"
            )
        }
    }
}

data class MainUiState(
    val userName: String = "Гость",
    val isLoading: Boolean = false
)