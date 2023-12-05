package com.core.domain.models.notes

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.time.LocalDate
import java.util.*

@Root(name = "nota_con_subnotas")
class NotaConSubnota(
    uuid: UUID = UUID.randomUUID(),
    checkBox: Boolean = false,
    fechaCreacion: String = LocalDate.now().toString(),

    @field:ElementList(name = "lista_subnotas", inline = true)
    @param:ElementList(name = "lista_subnotas", inline = true)
    var subnotaList: MutableList<Subnota> = mutableListOf(),
) : Nota(uuid, checkBox, fechaCreacion) {
    override fun toString(): String {
        return "NotaConSubnota(uuid: ${uuid}, subnota=$subnotaList)"
    }
}
