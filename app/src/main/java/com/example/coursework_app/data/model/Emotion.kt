package com.example.coursework_app.data.model

import android.media.Image

data class Emotion(
    val id: String,
    val name: EmotionType,
    val emoji: Image
)