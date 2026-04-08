package com.example.coursework_app.data.db.entity.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.coursework_app.data.db.converters.NoteEntityConverter
import kotlinx.serialization.Contextual

@Entity(
    tableName = "notes",
    indices = [Index(value = [NoteEntityDb.Column.ID])]
)
// изучить для чего
@TypeConverters(NoteEntityConverter::class)
data class NoteEntityDb(

    @PrimaryKey
    @ColumnInfo(name = Column.ID)
    val id: String,

    // поменять на enum
    @ColumnInfo(name = "title")
    val emotion: String,

    // string -> DateTime
    val notes: @Contextual Map<String, DayNoteDb>,
) {

    object Column {

        const val ID: String = "id"
    }
}
