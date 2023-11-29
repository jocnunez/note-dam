package com.dam.ad.notedam.repositories.categories

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.*

class RepositoryCategoriesMemory: IRepositoryCategories{
    private val categories: MutableMap<UUID, Category> = mutableMapOf()

    override fun findByTitle(title: String): Result<Category, CategoryError> {
        val category = categories.values.find { it.title == title }
        return if (category != null) {
            Ok(category)
        } else {
            Err(CategoryError.CategoryNotFound())
        }
    }

    override fun findByPriority(priority: UInt): Result<Category, CategoryError> {
        val category = categories.values.find { it.priority == priority }
        return if (category != null) {
            Ok(category)
        } else {
            Err(CategoryError.CategoryNotFound())
        }
    }

    override fun getNotesSortedByPriority(category: Category): Iterable<Category> {
        return categories.values.sortedBy { it.priority }
    }

    override fun save(element: Category): Result<Category, CategoryError> {
        categories[element.uuid] = element
        return if(categories[element.uuid] == element){
            Ok(element)
        } else {
            Err(CategoryError.CategoryNotSaved())
        }
    }

    override fun saveAll(elements: Iterable<Category>) {
        elements.forEach { save(it) }
    }

    override fun deleteById(id: UUID): Result<Boolean, CategoryError> {
        val result = categories.remove(id) != null
        return if (result) {
            Ok(true)
        } else {
            Err(CategoryError.CategoryNotDeleted())
        }
    }

    override fun delete(element: Category): Result<Boolean, CategoryError> {
        return deleteById(element.uuid)
    }

    override fun deleteAll() {
        categories.clear()
    }

    override fun findAll(): Iterable<Category> {
        return categories.values
    }

    override fun findById(id: UUID): Result<Category, CategoryError> {
        val category = categories[id]
        return if (category != null) {
            Ok(category)
        } else {
            Err(CategoryError.CategoryNotFound())
        }
    }

    override fun existsById(id: UUID): Boolean {
        return categories.containsKey(id)
    }

    override fun count(): Int {
        return categories.size
    }
}