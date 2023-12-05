package com.dam.ad.notedam.models

import java.time.LocalDate

sealed class Todo {
    abstract val title: String
    abstract val createdAt: LocalDate
    abstract val type: TodoType
    abstract var completed: Boolean

    enum class TodoType { TEXT, IMAGE, AUDIO, SUBLIST }

    data class TextTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val text: String
    ) : Todo() {
        override val type = TodoType.TEXT
    }

    data class ImageTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val image: String
    ) : Todo() {
        override val type = TodoType.IMAGE
    }

    data class AudioTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val audio: String
    ) : Todo() {
        override val type = TodoType.AUDIO
    }

    data class SublistTodo(
        override val title: String,
        override val createdAt: LocalDate,
        override var completed: Boolean,
        val sublist: MutableList<SublistItem>
    ) : Todo() {
        override val type = TodoType.SUBLIST
    }
}

data class SublistItem(
    val title: String,
    val completed: Boolean = false
)
