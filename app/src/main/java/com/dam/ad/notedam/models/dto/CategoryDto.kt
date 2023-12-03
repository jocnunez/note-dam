package com.dam.ad.notedam.models.dto

data class CategoryDto(
    val uuid: String,
    val title: String,
    val description: String,
    val priority: UInt,
    val notes: List<NoteDto>
)