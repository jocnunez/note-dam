package com.dam.ad.notedam.Models.nota

import android.net.Uri
import com.dam.ad.notedam.Enums.NotaType
import java.net.URI
import java.util.*

data class NotaImagen (var uriImagen : Uri?, var textoNota: String, override val uuid: UUID = UUID.randomUUID(),
                       override var prioridad : Int = 999999999) : Nota() {
    override val tipoNota = NotaType.Imagen
}