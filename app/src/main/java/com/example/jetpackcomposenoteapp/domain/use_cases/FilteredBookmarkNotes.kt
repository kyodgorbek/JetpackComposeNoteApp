package com.example.jetpackcomposenoteapp.domain.use_cases

import com.example.jetpackcomposenoteapp.data.local.model.Note
import com.example.jetpackcomposenoteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilteredBookmarkNotes @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getBookMarkedNotes()
    }
}