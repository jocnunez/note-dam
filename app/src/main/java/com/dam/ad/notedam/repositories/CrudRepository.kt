package com.dam.ad.notedam.repositories

import com.github.michaelbull.result.Result

interface CrudRepository<T, ID, ERR>: SimpleCrud<T, ID, ERR> {
    fun save(element: T): Result<T, ERR>
    fun saveAll(elements: Iterable<T>)
    fun deleteById(id: ID): Result<Boolean, ERR>
    fun delete(element: T): Result<Boolean, ERR>
    fun deleteAll()
}