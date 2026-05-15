package com.example.coursework_app.data.db.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.coursework_app.data.db.entity.notes.DayNoteDb
import com.example.coursework_app.domain.model.notes.EmotionType
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.time.LocalDate
import javax.inject.Inject

@ProvidedTypeConverter
class NoteEntityConverter @Inject constructor(
    private val json: Json,
) {

    @TypeConverter
    fun fromEmotionType(value: EmotionType): String = value.name

    @TypeConverter
    fun toEmotionType(value: String): EmotionType = EmotionType.parseStored(value)

    @TypeConverter
    fun fromNotesMap(value: Map<LocalDate, DayNoteDb>): String {
        val payload = NotesSerialized(
            entries = value.map { (date, note) ->
                NoteDaySerialized(date = date.toString(), note = note)
            },
        )
        return json.encodeToString(payload)
    }

    @TypeConverter
    fun toNotesMap(value: String): Map<LocalDate, DayNoteDb> {
        if (value.isBlank()) return emptyMap()
        val root = json.parseToJsonElement(value)
        return when {
            root is JsonObject && root.containsKey(ENTRIES_KEY) -> {
                val payload = json.decodeFromString<NotesSerialized>(value)
                payload.entries.associate { entry ->
                    LocalDate.parse(entry.date) to entry.note
                }
            }
            root is JsonObject -> decodeLegacyStringKeyedMap(value)
            else -> emptyMap()
        }
    }

    private fun decodeLegacyStringKeyedMap(value: String): Map<LocalDate, DayNoteDb> {
        val legacy = json.decodeFromString<Map<String, DayNoteDb>>(value)
        return legacy.mapNotNull { (key, note) ->
            runCatching { LocalDate.parse(key) }.getOrNull()?.let { it to note }
        }.toMap()
    }

    private companion object {
        const val ENTRIES_KEY = "entries"
    }
}

@Serializable
private data class NotesSerialized(
    val entries: List<NoteDaySerialized>,
)

@Serializable
private data class NoteDaySerialized(
    val date: String,
    val note: DayNoteDb,
)
