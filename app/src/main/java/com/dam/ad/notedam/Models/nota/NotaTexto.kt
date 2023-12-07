package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType

class NotaTexto (
    var textoNota : String
) : Nota() {
    val tipoNota = NotaType.Texto
}