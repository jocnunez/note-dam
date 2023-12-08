package com.dam.ad.notedam.repositories

import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.errors.NotaError
import com.github.michaelbull.result.Result
import java.util.*

class NotaRepository (val activity: MainActivity , val uuidCategoria : UUID) : INotaRepository {

    override fun addItem(item: Nota): Result<Nota, NotaError> {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: Nota): Result<Nota, NotaError> {
        TODO("Not yet implemented")
    }

    override fun loadAllItems(): Result<MutableList<Nota>, NotaError> {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: Nota): Result<Nota, NotaError> {
        TODO("Not yet implemented")
    }

    override fun addAllItems(items: MutableList<Nota>): Result<MutableList<Nota>, NotaError> {
        TODO("Not yet implemented")
    }
}