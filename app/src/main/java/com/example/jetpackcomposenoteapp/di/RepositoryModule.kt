package com.example.jetpackcomposenoteapp.di

import com.example.jetpackcomposenoteapp.data.repository.NoteRepositoryImpl
import com.example.jetpackcomposenoteapp.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository
}