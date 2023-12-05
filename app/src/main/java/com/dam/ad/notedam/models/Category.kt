package com.dam.ad.notedam.models

import java.time.LocalDate

data class Category(
    val name: String,
    val createdAt: LocalDate,
    val todos: MutableList<Todo>
)
