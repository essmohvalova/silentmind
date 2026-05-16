package com.example.coursework_app.domain.repository

import com.example.coursework_app.domain.model.notes.Note
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NotesRepository {

    suspend fun saveNote(note: Note)

    suspend fun getNoteByDate(date: LocalDate): Note?

    fun observeNotes(): Flow<Note?>
}
