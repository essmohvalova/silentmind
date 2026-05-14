package com.example.coursework_app.domain.model.notes

import java.util.Locale

/**
 * Type-safe emotion values stored for notes (Room / domain).
 * Matches legacy domain emotion labels and [EmotionType.name] for persistence.
 */
enum class EmotionType {
    JOY,
    CALM,
    SADNESS,
    ANXIETY,
    ANGER,
    FATIGUE;

    companion object {

        /**
         * Parses persisted value: enum [name], legacy Russian labels, or lowercase ids (e.g. "joy").
         */
        fun parseStored(raw: String): EmotionType {
            val value = raw.trim()
            if (value.isEmpty()) return JOY
            runCatching { valueOf(value) }.getOrNull()?.let { return it }
            return fromLegacyLabel(value)
        }

        private fun fromLegacyLabel(value: String): EmotionType {
            val lower = value.lowercase(Locale.ROOT)
            return when (value) {
                "Радость" -> JOY
                "Спокойствие" -> CALM
                "Грусть" -> SADNESS
                "Тревога" -> ANXIETY
                "Злость" -> ANGER
                "Усталость" -> FATIGUE
                else -> when (lower) {
                    "joy" -> JOY
                    "calm" -> CALM
                    "sadness" -> SADNESS
                    "anxiety" -> ANXIETY
                    "anger" -> ANGER
                    "fatigue" -> FATIGUE
                    else -> JOY
                }
            }
        }
    }
}
