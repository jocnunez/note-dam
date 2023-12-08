package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType
import java.util.*

data class NotaTexto(
    var textoNota: String,
    override val uuid: UUID = UUID.randomUUID(),
    override var prioridad : Int = 999999999
) : Nota() {
    override val tipoNota = NotaType.Texto
}