package com.example.coursework_app.ui.emotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.emotion.Emotion
import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.preferences.UserPreferences
import com.example.coursework_app.domain.repository.MoodEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EmotionViewModel @Inject constructor(
    private val moodEntryRepository: MoodEntryRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    var selectedEmotion: Emotion? = null
        private set

    var intensity: Int = 3
        private set

    var text: String = ""
        private set

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun selectEmotion(emotion: Emotion) {
        selectedEmotion = emotion
    }

    fun changeIntensity(value: Int) {
        intensity = value
    }

    fun changeText(value: String) {
        text = value
    }

    fun save() {
        val userId = userPreferences.getUserId()
        val emotion = selectedEmotion

        if (userId == null || emotion == null) return

        viewModelScope.launch {
            runCatching {
                moodEntryRepository.saveMoodEntry(
                    MoodEntry(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        emotion = emotion.text,
                        intensity = intensity,
                        text = text.takeIf { it.isNotBlank() },
                        createdAt = System.currentTimeMillis(),
                    )
                )
            }.onSuccess {
                _events.emit(Event.Saved)
            }.onFailure { throwable ->
                _events.emit(Event.SaveFailed(throwable.message ?: "Unknown save error"))
            }
        }
    }

    sealed interface Event {
        data object Saved : Event
        data class SaveFailed(val reason: String) : Event
    }
}