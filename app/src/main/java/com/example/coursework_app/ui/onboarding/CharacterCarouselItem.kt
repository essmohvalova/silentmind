package com.example.coursework_app.ui.onboarding

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.example.coursework_app.domain.model.user.CharacterType

data class CharacterCarouselItem(
    val type: CharacterType,
    @RawRes val lottieRes: Int,
    @StringRes val displayNameRes: Int,
)
