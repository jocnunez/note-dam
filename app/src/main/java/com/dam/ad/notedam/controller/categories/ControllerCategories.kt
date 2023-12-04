package com.dam.ad.notedam.controller.categories

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.models.errors.NoteError
import com.dam.ad.notedam.repositories.categories.IRepositoryCategories
import com.dam.ad.notedam.services.storage.categories.CategoryStorageService
import com.dam.ad.notedam.utils.validators.validate
import com.github.michaelbull.result.*
import java.util.*

class ControllerCategories(
    private val repo: IRepositoryCategories,
    private val storage: CategoryStorageService,
    private val filePath: String,
): IControllerCategories{
    private var categorySelected: Category? = null

    override fun setCategorySelected(category: Category?) {
        categorySelected = category
    }

    override fun getCategorySelected(): Category? {
        return categorySelected
    }

    override fun addNoteToSelectedCategory(note: Note<*>): Result<Note<*>, NoteError> {
        // Falta validar que la nota
        if (categorySelected == null) return Err(NoteError.NoteNotSaved())
        categorySelected?.notes?.set(note.uuid, (note))
        save(categorySelected!!).mapBoth(
            success = { return Ok(note) },
            failure = { return Err(NoteError.NoteNotSaved()) }
        )
    }

    override fun removeNoteFromSelectedCategory(note: Note<*>): Result<Note<*>, NoteError> {
        if (categorySelected == null) return Err(NoteError.NoteNotDeleted())
        categorySelected?.notes?.remove(note.uuid)
        save(categorySelected!!).mapBoth(
            success = { return Ok(note) },
            failure = { return Err(NoteError.NoteNotDeleted()) }
        )
    }

    override fun addItemToSublist(note: Note.Sublist, item: SublistItem): Result<SublistItem, NoteError> {
        (categorySelected?.notes?.get(note.uuid) as Note.Sublist).sublist[item.subListValue] = item
        save(categorySelected!!).mapBoth(
            success = { return Ok(item) },
            failure = { return Err(NoteError.NoteNotDeleted()) })
    }

    override fun removeItemFromSublist(note: Note.Sublist, item: SublistItem): Result<SublistItem, NoteError> {
        (categorySelected?.notes?.get(note.uuid) as Note.Sublist).sublist.remove(item.subListValue)
        save(categorySelected!!).mapBoth(
            success = { return Ok(item) },
            failure = { return Err(NoteError.NoteNotDeleted()) })
    }

    override fun loadAll(clearBefore: Boolean): Result<Iterable<Category>, CategoryError> {
        if (clearBefore) repo.deleteAll()
        val categories = storage.loadAll(filePath).onSuccess {
            repo.saveAll(it)
        }
        return categories
    }

    override fun exportAll(): Result<Iterable<Category>, CategoryError> {
        return storage.exportAll(repo.findAll(), filePath)
    }

    override fun save(element: Category): Result<Category, CategoryError> {
        return element.validate().andThen {
            repo.save(element).onSuccess {
                storage.export(element, filePath)
            }
        }
    }

    override fun saveAll(elements: Iterable<Category>) {
        elements.forEach { it.validate().onFailure { return } }
        repo.saveAll(elements)
        exportAll()
    }

    override fun deleteById(id: UUID): Result<Boolean, CategoryError> {
        return repo.deleteById(id).onSuccess {
            storage.exportAll(repo.findAll(), filePath, true)
        }
    }

    override fun delete(element: Category): Result<Boolean, CategoryError> {
        return repo.delete(element).onSuccess {
            storage.exportAll(repo.findAll(), filePath, true)
        }
    }

    override fun deleteAll() {
        repo.deleteAll()
        storage.exportAll(repo.findAll(), filePath, true)
    }

    override fun findAll(): Iterable<Category> {
        return repo.findAll()
    }

    override fun findById(id: UUID): Result<Category, CategoryError> {
        return repo.findById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return repo.existsById(id)
    }

    override fun count(): Int {
        return repo.count()
    }
}