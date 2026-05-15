package com.example.coursework_app.ui.onboarding

import com.example.coursework_app.R
import com.example.coursework_app.domain.model.user.CharacterType

/**
 * Единый источник списка персонажей для карусели онбординга (порядок = порядок в списке).
 */
object CharacterCarouselItemsFactory {

    fun create(): List<CharacterCarouselItem> = listOf(
        CharacterCarouselItem(
            type = CharacterType.CAT,
            lottieRes = R.raw.character_cat,
            displayNameRes = R.string.character_cat,
        ),
        CharacterCarouselItem(
            type = CharacterType.DOG,
            lottieRes = R.raw.character_dog,
            displayNameRes = R.string.character_dog,
        ),
        CharacterCarouselItem(
            type = CharacterType.EMOJI,
            lottieRes = R.raw.character_emoji,
            displayNameRes = R.string.character_emoji,
        ),
    )
}
