package com.dam.ad.notedam.services.storage

import com.github.michaelbull.result.Result

interface StorageService<T, ERR> {
    fun export(element: T, filePath: String, clearFiles: Boolean = false): Result<T, ERR>
    fun exportAll(elements: Iterable<T>, filePath: String, clearFiles: Boolean = false): Result<Iterable<T>, ERR>
    fun loadAll(filePath: String): Result<List<T>, ERR>
}