package com.dam.ad.notedam.Models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.util.UUID

data class Categoria(
    @JacksonXmlProperty(localName = "uuid")
    val uuid: UUID = UUID.randomUUID(),
    @JacksonXmlProperty(localName = "nombreCategoria")
    var nombreCategoria: String,
    @JacksonXmlProperty(localName = "prioridadCategoria")
    var prioridadCategoria: Int = 999999
) {
    fun bajarCategoria() { prioridadCategoria -= 1 }
    fun subirCategoria() { prioridadCategoria += 1 }
}