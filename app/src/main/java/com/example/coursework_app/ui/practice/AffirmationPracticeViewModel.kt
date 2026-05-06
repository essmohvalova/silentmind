package com.example.coursework_app.ui.practice

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.R
import com.example.coursework_app.domain.repository.MoodEntryRepository
import com.example.coursework_app.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AffirmationPracticeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getUserUseCase: GetUserUseCase,
    private val moodEntryRepository: MoodEntryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AffirmationUiState())
    val uiState: StateFlow<AffirmationUiState> = _uiState

    private var observeJob: Job? = null

    init {
        observeLastMood()
    }

    private fun observeLastMood() {
        observeJob?.cancel()
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val userId = getUserUseCase()?.id
            if (userId == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        emotionLabel = null,
                        quote = defaultQuote(),
                        hint = "Пользователь не найден"
                    )
                }
                return@launch
            }

            observeJob = launch {
                moodEntryRepository.observeMoodEntries(userId).collectLatest { entries ->
                    val lastEmotion = entries.firstOrNull()?.emotion
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            emotionLabel = lastEmotion,
                            quote = quoteForEmotion(lastEmotion),
                            hint = if (lastEmotion == null) {
                                "Сначала добавьте запись настроения, чтобы получить персональную аффирмацию"
                            } else {
                                null
                            }
                        )
                    }
                }
            }
        }
    }

    private fun quoteForEmotion(emotion: String?): String {
        val key = emotion?.trim()?.lowercase().orEmpty()
        val quotes = when (key) {
            "радость" -> quotesFrom(R.array.affirmations_joy)
            "спокойствие" -> quotesFrom(R.array.affirmations_calm)
            "грусть" -> quotesFrom(R.array.affirmations_sadness)
            "тревога" -> quotesFrom(R.array.affirmations_anxiety)
            "злость" -> quotesFrom(R.array.affirmations_anger)
            "усталость" -> quotesFrom(R.array.affirmations_fatigue)
            else -> emptyList()
        }
        return quotes.randomOrNull() ?: defaultQuote()
    }

    private fun quotesFrom(@ArrayRes id: Int): List<String> {
        return context.resources.getStringArray(id).toList()
    }

    private fun defaultQuote(): String {
        return context.getString(R.string.affirmation_default_quote)
    }
}

data class AffirmationUiState(
    val isLoading: Boolean = true,
    val emotionLabel: String? = null,
    val quote: String = "",
    val hint: String? = null,
)
