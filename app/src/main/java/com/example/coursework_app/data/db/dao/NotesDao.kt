package com.example.coursework_app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coursework_app.data.db.entity.notes.NoteEntityDb

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(user: NoteEntityDb)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNote(id: String): NoteEntityDb

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<NoteEntityDb>
}
