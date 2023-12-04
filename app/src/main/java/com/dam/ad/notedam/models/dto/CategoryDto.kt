package com.dam.ad.notedam.models.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "category")
data class CategoryDto(
    @field:JacksonXmlProperty(localName = "uuid")
    @param:JacksonXmlProperty(localName = "uuid")
    val uuid: String = "",
    @field:JacksonXmlProperty(localName = "title")
    @param:JacksonXmlProperty(localName = "title")
    val title: String = "",
    @field:JacksonXmlProperty(localName = "description")
    @param:JacksonXmlProperty(localName = "description")
    val description: String = "",
    @field:JacksonXmlProperty(localName = "level")
    @param:JacksonXmlProperty(localName = "level")
    val level: Int = 0,
    @field:JacksonXmlProperty(localName = "notes")
    @param:JacksonXmlProperty(localName = "notes")
    val notes: List<NoteDto> = listOf()
){
    // Constructor por defecto
    @JsonCreator // Importa esta anotación de com.fasterxml.jackson.annotation.JsonCreator
    constructor() : this("", "", "", 0, listOf())
}