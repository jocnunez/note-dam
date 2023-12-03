package com.dam.ad.notedam.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Root(name= "category")
data class Category(
    @field:Attribute(name ="uuid")
    @param:Attribute(name = "uuid")
    val uuid: UUID = UUID.randomUUID(),

    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String,

    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String,

    @field:Element(name="priority")
    @param:Element(name = "priority")
    val priority: UInt,

    @field:ElementList(name="notes")
    @param:ElementList(name = "notes")
    val notes: Iterable<Note<*>>
)
