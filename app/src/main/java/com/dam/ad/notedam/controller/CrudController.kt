package com.dam.ad.notedam.controller

import com.dam.ad.notedam.repositories.CrudRepository
import com.github.michaelbull.result.Result

interface CrudController<T, ID, ERR>: CrudRepository<T, ID, ERR> {
    fun loadAll(clearBefore: Boolean = true): Result<Iterable<T>, ERR>
    fun exportAll(): Result<Iterable<T>, ERR>
}