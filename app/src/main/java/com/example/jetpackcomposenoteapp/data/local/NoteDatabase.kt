package com.example.jetpackcomposenoteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jetpackcomposenoteapp.data.local.NoteDao
import com.example.jetpackcomposenoteapp.data.local.converters.DateConverter
import com.example.jetpackcomposenoteapp.data.local.model.Note

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}