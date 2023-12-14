package com.dam.ad.notedam.Models.nota

import com.dam.ad.notedam.Enums.NotaType
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.google.gson.annotations.SerializedName
import java.util.*

sealed class Nota (
    @SerializedName("uuid")
    open val uuid: UUID = UUID.randomUUID(),
    @SerializedName("prioridad")
    open var prioridad : Int = 9999999
) {
    @SerializedName("tipoNota")
    open val tipoNota : NotaType? =  null
}