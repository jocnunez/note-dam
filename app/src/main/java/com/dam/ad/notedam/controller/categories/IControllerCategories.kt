package com.dam.ad.notedam.controller.categories

import com.dam.ad.notedam.controller.CrudController
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.models.errors.NoteError
import com.github.michaelbull.result.Result
import java.util.UUID

interface IControllerCategories: CrudController<Category, UUID, CategoryError>{
    fun setCategorySelected(category: Category?)
    fun getCategorySelected(): Category?

    fun addNoteToSelectedCategory(note: Note<*>): Result<Note<*>, NoteError>
    fun removeNoteFromSelectedCategory(note: Note<*>): Result<Note<*>, NoteError>
    fun addItemToSublist(note: Note.Sublist, item: SublistItem): Result<SublistItem, NoteError>
    fun removeItemFromSublist(note: Note.Sublist, item: SublistItem): Result<SublistItem, NoteError>
}