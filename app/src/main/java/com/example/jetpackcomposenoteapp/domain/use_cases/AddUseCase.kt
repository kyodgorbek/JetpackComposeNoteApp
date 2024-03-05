package com.example.jetpackcomposenoteapp.domain.use_cases

import com.example.jetpackcomposenoteapp.data.local.model.Note
import com.example.jetpackcomposenoteapp.domain.repository.NoteRepository
import javax.inject.Inject

class AddUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = noteRepository.insert(note)
}