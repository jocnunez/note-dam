package com.dam.ad.notedam.models

import java.util.UUID

data class Category(
    val uuid: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val priority: UInt,
    val notes: Iterable<Note<*>>
)
