package com.example.coursework_app.ui.emotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.emotion.Emotion
import com.example.coursework_app.domain.usecase.SaveMoodEntryResult
import com.example.coursework_app.domain.usecase.SaveMoodEntryUseCase
import com.example.coursework_app.ui.emotion.mapper.EmotionUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmotionViewModel @Inject constructor(
    private val saveMoodEntryUseCase: SaveMoodEntryUseCase,
    private val emotionUiMapper: EmotionUiMapper,
) : ViewModel() {

    val availableEmotionsUi: List<EmotionUi> =
        emotionUiMapper.toUiList(Emotion.defaultList)

    private val _uiState = MutableStateFlow(EmotionUiState())
    val uiState: StateFlow<EmotionUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun selectEmotion(emotionUi: EmotionUi) {
        _uiState.update {
            it.copy(selectedEmotion = emotionUiMapper.toDomain(emotionUi))
        }
    }

    fun changeIntensity(value: Int) {
        _uiState.update { it.copy(intensity = value) }
    }

    fun changeText(value: String) {
        _uiState.update { it.copy(text = value) }
    }

    fun save() {
        viewModelScope.launch {
            val snapshot = _uiState.value
            _uiState.update { it.copy(isSaving = true) }

            try {
                val result = saveMoodEntryUseCase(
                    emotionName = snapshot.selectedEmotion?.text,
                    intensity = snapshot.intensity,
                    noteText = snapshot.text,
                )

                when (result) {
                    SaveMoodEntryResult.Success -> {
                        _events.emit(Event.Saved)
                    }

                    SaveMoodEntryResult.NoLoggedInUser -> {
                        _events.emit(Event.SaveFailed("Пользователь не авторизован"))
                    }

                    SaveMoodEntryResult.NoEmotionSelected -> {
                        _events.emit(Event.SaveFailed("Эмоция не выбрана"))
                    }

                    is SaveMoodEntryResult.Failure -> {
                        _events.emit(Event.SaveFailed(result.message))
                    }
                }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    sealed interface Event {
        data object Saved : Event
        data class SaveFailed(val reason: String) : Event
    }
}
