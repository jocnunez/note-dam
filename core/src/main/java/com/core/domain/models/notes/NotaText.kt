package com.core.domain.models.notes

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.time.LocalDate
import java.util.*

@Root(name = "nota_text")
class NotaText(
    uuid: UUID = UUID.randomUUID(),
    checkBox: Boolean = false,
    fechaCreacion: String = LocalDate.now().toString(),

    @field:Element(name = "texto")
    @param:Element(name = "texto")
    var texto: String = ""
) : Nota(uuid, checkBox, fechaCreacion) {
    override fun toString(): String {
        return "NotaText(checkBox=$checkBox,texto='$texto')"
    }
}
