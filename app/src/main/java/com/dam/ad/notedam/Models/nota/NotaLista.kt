package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType

class NotaLista (var textoNota : String, var lista : MutableList<SubList>) : Nota() {
    override val tipoNota = NotaType.Lista
}