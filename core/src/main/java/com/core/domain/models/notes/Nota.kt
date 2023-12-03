package com.core.domain.models.notes

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.time.LocalDate
import java.util.*

@Root(name = "nota")
abstract class Nota(
    @field:Element(name = "uuid")
    @param:Element(name = "uuid")
    var uuid: UUID = UUID.randomUUID(),

    @field:Element(name = "esta_marcada")
    @param:Element(name = "esta_marcada")
    var checkBox: Boolean = false,

    @field:Element(name = "fecha_creacion")
    @param:Element(name = "fecha_creacion")
    var fechaCreacion: String = LocalDate.now().toString()
) {
    override fun toString(): String {
        return "Nota(checkBox=$checkBox, fechaCreacion='$fechaCreacion')"
    }
}
