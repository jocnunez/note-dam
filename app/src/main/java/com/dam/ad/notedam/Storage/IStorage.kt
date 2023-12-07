package com.dam.ad.notedam.Storage

import java.util.*

interface IStorage <T>{
    fun loadAllItems(uuid : UUID) : MutableList<T>
    fun saveAllItems(path : UUID, listaItems : MutableList<T>)
}