package com.example.coursework_app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.usecase.LoadMainUserNameUseCase
import com.example.coursework_app.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadMainUserNameUseCase: LoadMainUserNameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            runSuspendCatching {
                loadMainUserNameUseCase()
            }.onSuccess { displayName ->
                _uiState.value = _uiState.value.copy(
                    userName = displayName,
                )
            }
        }
    }
}

data class MainUiState(
    val userName: String = "Гость",
    val isLoading: Boolean = false,
)
