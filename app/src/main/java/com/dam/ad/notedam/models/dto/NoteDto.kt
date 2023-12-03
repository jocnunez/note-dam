package com.dam.ad.notedam.models.dto

import com.dam.ad.notedam.models.SublistItem

data class NoteDto(
    val uuid: String,
    val type: String,
    val fechaCreate: String,
    val check: Boolean,

    val text: String? = null,
    val image: String? = null,
    val audio: String? = null,
    val subList: List<SublistItem>? = null
)