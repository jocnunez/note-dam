package com.dam.ad.notedam.models

import java.time.LocalDate
import java.util.UUID

data class Category(
    val uuid: UUID,
    val name: String,
    val createdAt: LocalDate,
    val todos: MutableList<Todo>
) {
    override fun toString(): String = name
}
