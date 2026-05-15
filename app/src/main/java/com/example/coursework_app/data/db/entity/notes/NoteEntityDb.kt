package com.example.coursework_app.data.db.entity.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.coursework_app.data.db.converters.NoteEntityConverter
import com.example.coursework_app.domain.model.notes.EmotionType
import java.time.LocalDate

@Entity(
    tableName = "notes",
    indices = [Index(value = [NoteEntityDb.Column.ID])],
)
@TypeConverters(NoteEntityConverter::class)
data class NoteEntityDb(
    @PrimaryKey
    @ColumnInfo(name = Column.ID)
    val id: String,

    @ColumnInfo(name = "emotion")
    val emotion: EmotionType,

    val notes: Map<LocalDate, DayNoteDb>,
) {
    object Column {
        const val ID: String = "id"
    }
}
