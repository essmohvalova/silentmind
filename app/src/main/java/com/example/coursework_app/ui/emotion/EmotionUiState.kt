package com.example.coursework_app.ui.emotion

data class EmotionUiState(
    val selectedEmotion: String? = null,
    val intensity: Int = 3,
    val text: String = "",
    val isSaving: Boolean = false,
    val isSaveEnabled: Boolean = false,
)