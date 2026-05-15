package com.example.coursework_app.ui.emotion.mapper

import com.example.coursework_app.R
import com.example.coursework_app.domain.model.emotion.Emotion
import com.example.coursework_app.ui.emotion.EmotionUi
import javax.inject.Inject

class EmotionUiMapper @Inject constructor() {

    fun toUi(emotion: Emotion): EmotionUi =
        EmotionUi(
            id = emotion.id,
            text = emotion.text,
            iconRes = iconResForId(emotion.id),
        )

    fun toUiList(emotions: List<Emotion>): List<EmotionUi> = emotions.map(::toUi)

    fun toDomain(ui: EmotionUi): Emotion =
        Emotion(id = ui.id, text = ui.text)

    private fun iconResForId(id: String): Int =
        when (id) {
            "joy" -> R.drawable.ic_happy_smile
            "calm" -> R.drawable.ic_calm_smile
            "sadness" -> R.drawable.ic_sad_smile
            "anxiety" -> R.drawable.ic_anxiety_smile
            "anger" -> R.drawable.ic_angry_smile
            "fatigue" -> R.drawable.ic_tired_smile
            else -> R.drawable.ic_happy_smile
        }
}
