package com.example.coursework_app.ui.emotion

import androidx.annotation.DrawableRes

data class EmotionUi(
    val id: String,
    val text: String,
    @DrawableRes val iconRes: Int,
)
