package com.dam.ad.notedam.models

import java.time.LocalDate

sealed class Todo {
    abstract val title: String
    abstract val createdAt: LocalDate
    abstract var completed: Boolean

    data class TextTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val text: String
    ) : Todo()

    data class ImageTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val image: String
    ) : Todo()

    data class AudioTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val audio: String
    ) : Todo()

    data class SublistTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val sublist: Sublist
    ) : Todo() {
        val items = sublist.items
    }
}

data class Sublist(val items: MutableList<SublistItem>) {
    fun add(title: String, completed: Boolean = false) = items.add(SublistItem(title, completed))
}

data class SublistItem(
    val title: String,
    val completed: Boolean = false
)
