package com.dam.ad.notedam.Models.nota

import android.net.Uri
import com.dam.ad.notedam.Enums.NotaType
import java.net.URI

class NotaImagen (val uriImagen : Uri?, val textoNota: String, ) : Nota() {
    val tipoNota = NotaType.Imagen
}