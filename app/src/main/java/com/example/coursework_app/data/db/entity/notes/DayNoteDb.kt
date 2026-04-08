package com.example.coursework_app.data.db.entity.notes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayNoteDb(

    @SerialName("text")
    val text: String,
)
