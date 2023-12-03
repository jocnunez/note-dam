package com.dam.ad.notedam.models.dto

import com.dam.ad.notedam.models.Category
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "categories")
data class CategoriesXmlDto (
    @field:ElementList( name ="category", inline = true)
    @param:ElementList( name ="category", inline = true)
    val categories: List<CategoryDto>
)