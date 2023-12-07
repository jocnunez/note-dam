package com.dam.ad.notedam.Models.nota

import java.util.*

sealed class Nota (
    val uuidNota: UUID = UUID.randomUUID(), var prioridad : Int = 9999999
) {
    fun bajarCategoria() { prioridad -= 1 }
    fun subirCategoria() { prioridad += 1 }
}