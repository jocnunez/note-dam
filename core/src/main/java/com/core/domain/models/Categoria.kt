package com.core.domain.models

import com.core.domain.models.notes.Nota
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.*

@Root(name = "categoria")
data class Categoria(
    @field:Element(name = "id")
    @param:Element(name = "id")
    val id: UUID = UUID.randomUUID(),

    @field:Element(name = "esta_seleccionada")
    @param:Element(name = "esta_seleccionada")
    val isChecked: Boolean,

    @field:Element(name = "prioridad")
    @param:Element(name = "prioridad")
    val prioridad: Int,

    @field:Element(name = "nombre")
    @param:Element(name = "nombre")
    val nombre: String,

    @field:ElementList(name = "lista_TODO", inline = true)
    @param:ElementList(name = "lista_TODO", inline = true)
    var notas: MutableList<Nota>
) {

    override fun toString(): String {
        return "Categoria(id=$id, isChecked=$isChecked, prioridad=$prioridad, nombre='$nombre', notas=$notas)"
    }

    fun addNotas(notas: List<Nota>) {
        this.notas.clear()
        this.notas.addAll(notas)
    }
}
