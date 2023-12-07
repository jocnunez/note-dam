package com.dam.ad.notedam.repositories

import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.errors.NotaError
import java.util.*

interface INotaRepository : ICRUDRepository<Nota, NotaError, UUID> {
}