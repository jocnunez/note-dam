package com.dam.ad.notedam.services.storage

import com.github.michaelbull.result.Result

interface StorageService<T, ERR> {
    fun saveAll(elements: Iterable<T>, filePath: String): Result<Iterable<T>, ERR>
    fun loadAll(filePath: String): Result<List<T>, ERR>
}