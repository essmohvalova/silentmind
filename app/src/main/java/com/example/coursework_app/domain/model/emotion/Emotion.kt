package com.example.coursework_app.domain.model.emotion

import androidx.annotation.DrawableRes
import com.example.coursework_app.R
data class Emotion(
    val id: String,
    val text: String,
    @DrawableRes val iconRes : Int,
) {
    companion object {
        val defaultList = listOf(
            Emotion(id = "joy", text = "Радость",   R.drawable.ic_happy_smile),
            Emotion(id = "calm", text = "Спокойствие", R.drawable.ic_calm_smile),
            Emotion(id = "sadness", text = "Грусть", R.drawable.ic_sad_smile),
            Emotion(id = "anxiety", text = "Тревога", R.drawable.ic_anxiety_smile),
            Emotion(id = "anger", text = "Злость", R.drawable.ic_angry_smile),
            Emotion(id = "fatigue", text = "Усталость",R.drawable.ic_tired_smile),
        )
    }
}
