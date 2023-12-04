package com.dam.ad.notedam.models

data class Category(
    val name: String,
    val todos: MutableList<Todo>
)
