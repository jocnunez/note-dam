package com.dam.ad.notedam.repositories

import com.github.michaelbull.result.Result

interface ICRUDRepository<T, ERR, ID> {
    fun addItem (item : T) : Result<T,ERR>
    fun deleteItem (item : T) : Result<T,ERR>
    fun loadAllItems () : Result<MutableList<T>,ERR>
    fun updateItem (item : T) : Result<T,ERR>
    fun addAllItems (items : MutableList<T>) : Result<MutableList<T>, ERR>
}