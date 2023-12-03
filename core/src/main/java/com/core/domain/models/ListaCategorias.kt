package com.core.domain.models

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "categorias")
class ListaCategorias(
    @field:ElementList(name = "categorias", inline = true, required = false)
    @param:ElementList(name = "categorias", inline = true, required = false)
    val categorias: List<Categoria> = listOf()
)