package com.core.domain.models.notes

import org.simpleframework.xml.Root
import java.time.LocalDate
import java.util.*

@Root(name = "subnota")
class Subnota(
    uuid: UUID = UUID.randomUUID(),
    checkBox: Boolean = false,
    fechaCreacion: String = LocalDate.now().toString()
) : Nota(uuid, checkBox, fechaCreacion) {

    override fun toString(): String {
        return "uuid: ${uuid}, checkbox: $checkBox, fechaCreacion: $fechaCreacion "
    }
}
