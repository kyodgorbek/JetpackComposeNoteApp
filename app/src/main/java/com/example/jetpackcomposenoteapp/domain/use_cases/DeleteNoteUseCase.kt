package com.example.jetpackcomposenoteapp.domain.use_cases

import com.example.jetpackcomposenoteapp.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(id: Long) = noteRepository.delete(id)
}