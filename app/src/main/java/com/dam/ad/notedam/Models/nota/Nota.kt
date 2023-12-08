package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType
import java.util.*

sealed class Nota (
    open val uuid: UUID = UUID.randomUUID(), open var prioridad : Int = 9999999
) {
    fun bajarCategoria() { prioridad -= 1 }
    fun subirCategoria() { prioridad += 1 }

    open val tipoNota : NotaType? =  null
}