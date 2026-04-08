package com.example.coursework_app.ui.emotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.usecase.GetUserUseCase
import com.example.coursework_app.domain.usecase.SaveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmotionViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmotionUiState())
    val uiState: StateFlow<EmotionUiState> = _uiState

    fun selectEmotion(emotionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedEmotionId = emotionId
            )

        }
    }
}

data class EmotionUiState(
    val selectedEmotionId: String? = null,
    val availableEmotions: List<String> = listOf("😊", "😢", "😠", "😌", "😨"),
    val isLoading: Boolean = false
)