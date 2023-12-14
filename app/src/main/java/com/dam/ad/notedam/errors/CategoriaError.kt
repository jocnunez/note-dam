package com.dam.ad.notedam.errors

sealed class CategoriaError(message : String) : Error (message)
class CategoriaFileEmptyError(message : String) : CategoriaError(message)
class CategoriaNotFoundError(message : String) : CategoriaError(message)
class CategoriaAlreadyExistsError(message : String) : CategoriaError(message)