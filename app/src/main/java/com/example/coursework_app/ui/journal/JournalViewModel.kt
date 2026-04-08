package com.example.coursework_app.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState

    fun loadEntries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                entriesCount = 42,  // Заглушка
                isLoading = false
            )
        }
    }
}

data class JournalUiState(
    val entriesCount: Int = 0,
    val isLoading: Boolean = true
)