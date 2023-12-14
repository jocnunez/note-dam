package com.dam.ad.notedam.Models.nota

import android.net.Uri
import com.dam.ad.notedam.Enums.NotaType
import com.google.gson.annotations.SerializedName
import java.util.*

data class NotaImagen(
    @SerializedName("uriImagen")
    var uriImagen: Uri?,
    @SerializedName("textoNotaImagen")
    var textoNota: String,
    override val uuid: UUID = UUID.randomUUID(),
    override var prioridad: Int = 999999999) : Nota() {
    override val tipoNota = NotaType.Imagen
}