package com.dam.ad.notedam.Models.DTO

import android.net.Uri
import com.dam.ad.notedam.Models.nota.*
import com.dam.ad.notedam.utils.listToSubList
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.util.UUID

@JacksonXmlRootElement(localName = "nota")
data class NotaDto(
    @field:JacksonXmlProperty(localName = "tipoNota")
    var tipoNota: String? = null,

    @field:JacksonXmlProperty(localName = "uuid")
    var uuid: String? = null,

    @field:JacksonXmlProperty(localName = "prioridad")
    var prioridad: String? = null,

    @field:JacksonXmlProperty(localName = "textoNota")
    var textoNota: String? = null,

    @field:JacksonXmlProperty(localName = "uriImagen")
    var uriImagen: String? = null,

    @field:JacksonXmlElementWrapper(localName = "lista")
    @field:JacksonXmlProperty(localName = "elemento")
    var lista: MutableList<SubListDto>? = null
) {

    fun toNota() : Nota {
        when (tipoNota) {
            "Texto" -> {
                return NotaTexto (
                    uuid = UUID.fromString(uuid),
                    textoNota = textoNota!!,
                    prioridad = prioridad!!.toInt()
                )
            }
            "Imagen" -> {
                return NotaImagen (
                    uuid = UUID.fromString(uuid),
                    textoNota = textoNota!!,
                    uriImagen = Uri.parse(uriImagen),
                    prioridad = prioridad!!.toInt(),
                )
            }
            "Lista" -> return NotaLista (
                uuid = UUID.fromString(uuid),
                textoNota = textoNota!!,
                prioridad = prioridad!!.toInt(),
                lista = lista!!.listToSubList()
            )
            else -> throw IllegalArgumentException("Tipo no Conocido")
        }
    }
}