package com.dam.ad.notedam.repositories.categories

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.repositories.CrudRepository
import com.github.michaelbull.result.Result
import java.util.UUID

interface IRepositoryCategories: CrudRepository<Category, UUID, CategoryError>{
    fun findByTitle(title: String): Result<Category, CategoryError>
    fun findByPriority(priority: UInt): Result<Category, CategoryError>
    fun getNotesSortedByPriority(): Iterable<Category>
}