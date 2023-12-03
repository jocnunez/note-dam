package com.dam.ad.notedam.presentation.adapter

import com.core.domain.models.notes.Nota

interface OnNoteClickListener {
    fun editNote(note: Nota)
    fun deleteNote(note: Nota)
    fun editCompleteNote(note: Nota)
}
