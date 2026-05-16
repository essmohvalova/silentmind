package com.example.coursework_app.ui.emotion

import com.example.coursework_app.domain.model.emotion.Emotion

data class EmotionUiState(
    val selectedEmotion: Emotion? = null,
    val intensity: Int = 3,
    val text: String = "",
    val isSaving: Boolean = false,
)
