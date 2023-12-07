package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType

class NotaLista (val textoNota : String, val lista : MutableList<SubList>) : Nota() {
    val tipoNota = NotaType.Lista
}