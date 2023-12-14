package com.dam.ad.notedam.errors

sealed class NotaError (message : String) : Error (message)
class NotaFileEmptyError(message : String) : NotaError(message)
class NotaNotFoundError(message : String) : NotaError(message)
class NotaAlreadyExistsError(message : String) : NotaError(message)