package com.dam.ad.notedam.models.dto

import com.dam.ad.notedam.models.SublistItem
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "note")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class NoteDto(
    @field:JacksonXmlProperty(localName = "noteId")
    @param:JacksonXmlProperty(localName = "noteId")
    val uuid: String = "",
    @field:JacksonXmlProperty(localName = "type")
    @param:JacksonXmlProperty(localName = "type")
    val type: String = "",
    @field:JacksonXmlProperty(localName = "fechaCreate")
    @param:JacksonXmlProperty(localName = "fechaCreate")
    val fechaCreate: String = "",
    @field:JacksonXmlProperty(localName = "check")
    @param:JacksonXmlProperty(localName = "check")
    val check: Boolean = false,
    @field:JacksonXmlProperty(localName = "text")
    @param:JacksonXmlProperty(localName = "text")
    val text: String? = null,
    @field:JacksonXmlProperty(localName = "image")
    @param:JacksonXmlProperty(localName = "image")
    val image: String? = null,
    @field:JacksonXmlProperty(localName = "audio")
    @param:JacksonXmlProperty(localName = "audio")
    val audio: String? = null,
    @field:JacksonXmlProperty(localName = "sublist")
    @param:JacksonXmlProperty(localName = "sublist")
    val subList: List<SublistItem>? = null
)