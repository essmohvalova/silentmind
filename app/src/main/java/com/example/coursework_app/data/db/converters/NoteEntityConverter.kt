package com.example.coursework_app.data.db.converters

import androidx.room.TypeConverter
import androidx.room.ProvidedTypeConverter
import com.example.coursework_app.data.db.entity.notes.DayNoteDb
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class NoteEntityConverter @Inject constructor(
    private val json: Json,
){

    @TypeConverter
    fun fromNotesMap(value: Map<String, DayNoteDb>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toNotesMap(value: String): Map<String, DayNoteDb> {
        return json.decodeFromString(value)
    }
}
