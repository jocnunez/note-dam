package com.dam.ad.notedam.repositories

import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.errors.CategoriaError
import java.util.*

interface ICategoriaRepository : ICRUDRepository<Categoria, CategoriaError, UUID>