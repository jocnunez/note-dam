package com.dam.ad.notedam.models.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

data class CategoryDto(
    @field:Element( name ="uuid")
    @param:Element( name ="uuid")
    val uuid: String,
    @field:Element( name ="title")
    @param:Element( name ="title")
    val title: String,
    @field:Element( name ="description")
    @param:Element( name ="description")

    val description: String,
    @field:Element( name ="priority")
    @param:Element( name ="priority")
    val priority: UInt,

    @field:ElementList( name ="notes", inline = true)
    @param:ElementList( name ="notes", inline = true)
    val notes: List<NoteDto>
)