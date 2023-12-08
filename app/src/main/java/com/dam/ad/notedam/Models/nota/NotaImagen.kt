package com.dam.ad.notedam.Models.nota

import android.net.Uri
import com.dam.ad.notedam.Enums.NotaType
import java.net.URI

class NotaImagen (var uriImagen : Uri?, var textoNota: String, ) : Nota() {
    override val tipoNota = NotaType.Imagen
}