package com.dam.ad.notedam.repositories

import com.github.michaelbull.result.Result

interface SimpleCrud<T, ID, ERR> {
    fun findAll(): Iterable<T>
    fun findById(id: ID): Result<T, ERR>
    fun existsById(id: ID): Boolean
    fun count(): Int
}