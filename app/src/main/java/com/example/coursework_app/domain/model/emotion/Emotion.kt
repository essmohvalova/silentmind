package com.example.coursework_app.domain.model.emotion

data class Emotion(
    val id: String,
    val text: String,
) {
    companion object {
        val defaultList = listOf(
            Emotion(id = "joy", text = "Радость"),
            Emotion(id = "calm", text = "Спокойствие"),
            Emotion(id = "sadness", text = "Грусть"),
            Emotion(id = "anxiety", text = "Тревога"),
            Emotion(id = "anger", text = "Злость"),
            Emotion(id = "fatigue", text = "Усталость"),
        )
    }
}
