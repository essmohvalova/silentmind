package com.example.coursework_app.data.repository

import com.example.coursework_app.data.db.dao.NotesDao
import com.example.coursework_app.domain.model.notes.Note
import com.example.coursework_app.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

// TODO() добавить мапперы, настроить di, добавить useCases
class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
) : NotesRepository {

    override suspend fun saveNote(note: Note) {
       // TODO() реализовать
    }

    override suspend fun getNoteByDateTime(dateTime: String): Note? {
        // TODO() реализовать
        return null
    }

    override fun observeNotes(): Flow<Note?> {
        // TODO() реализовать
        return emptyFlow()
    }


}
