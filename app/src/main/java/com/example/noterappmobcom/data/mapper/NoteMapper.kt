package com.example.noterappmobcom.data.mapper

import com.example.noterappmobcom.data.local.entity.NoteEntity
import com.example.noterappmobcom.domain.model.Note

fun NoteEntity.asExternalModel(): Note = Note(
    id, title, content
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id, title, content
)