package com.dam.ad.notedam.models.dto

import com.dam.ad.notedam.models.SublistItem
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name="note")
data class NoteDto(
    @field:Element( name ="uuid")
    @param:Element( name ="uuid")
    val uuid: String,

    @field:Element( name ="type")
    @param:Element( name ="type")
    val type: String,

    @field:Element( name ="fechaCreate")
    @param:Element( name ="fechaCreate")
    val fechaCreate: String,

    @field:Element( name ="check")
    @param:Element( name ="check")
    val check: Boolean,

    @field:Element( name ="text", required = false)
    @param:Element( name ="text", required = false)
    val text: String? = null,

    @field:Element( name ="image", required = false)
    @param:Element( name ="image", required = false)
    val image: String? = null,

    @field:Element( name ="audio", required = false)
    @param:Element( name ="audio", required = false)
    val audio: String? = null,

    @field:ElementList( name ="uuid", required = false)
    @param:ElementList( name ="uuid", required = false)
    val subList: List<SublistItem>? = null
)