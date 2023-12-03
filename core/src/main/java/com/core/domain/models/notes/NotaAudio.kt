package com.core.domain.models.notes

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.time.LocalDate
import java.util.*

@Root(name = "nota_audio")
class NotaAudio(
    uuid: UUID = UUID.randomUUID(),
    checkBox: Boolean = false,
    fechaCreacion: String = LocalDate.now().toString(),

    @field:Element(name = "audio")
    @param:Element(name = "audio")
    var audio: String = ""
) : Nota(uuid, checkBox, fechaCreacion) {

    override fun toString(): String {
        return "NotaAudio(audio='$audio')"
    }
}
