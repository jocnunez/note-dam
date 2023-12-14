package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType
import com.google.gson.annotations.SerializedName
import java.util.*

data class NotaLista (
    @SerializedName("textoNotaLista")
    var textoNota : String,
    var lista : MutableList<SubList>,
    override val uuid: UUID = UUID.randomUUID(),
    override var prioridad : Int = 999999999) : Nota() {

        override val tipoNota = NotaType.Lista


}