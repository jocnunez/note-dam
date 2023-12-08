package com.dam.ad.notedam.papelera

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "ToDoList")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class DTOToDoList(
    @field:JacksonXmlProperty(localName = "ToDoListName")
    @param:JacksonXmlProperty(localName = "ToDoListName")
    val name: String = "",
    @field:JacksonXmlProperty(localName = "ToDoListId")
    @param:JacksonXmlProperty(localName = "ToDoListId")
    val uuid: String = "",
    @field:JacksonXmlProperty(localName = "prioridad")
    @param:JacksonXmlProperty(localName = "prioridad")
    val prioridad: String = "",
    @field:JacksonXmlProperty(localName = "listaToDo")
    @param:JacksonXmlProperty(localName = "listaToDo")
    val listaToDo: List<String> = emptyList()
)