package com.example.jetpackcomposenoteapp.domain.use_cases

import com.example.jetpackcomposenoteapp.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: Long) = noteRepository.getNoteById(id)
}