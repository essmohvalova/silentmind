package com.example.coursework_app.data.model

data class Note(
    // @DocumentId
    val id: String, // user_id + number
    val user_id: Int,
    val emotion: String, //fk from emotion
    val emotion_level: Int, // scale from 1 to 10
    val tag_list: List<EmotionTags>,
    val additional_emotions: List<Emotion>
)
