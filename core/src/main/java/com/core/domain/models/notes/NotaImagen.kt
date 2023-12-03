package com.core.domain.models.notes

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.time.LocalDate
import java.util.*

@Root(name = "nota_imagen")
class NotaImagen(
    uuid: UUID = UUID.randomUUID(),
    checkBox: Boolean = false,
    fechaCreacion: String = LocalDate.now().toString(),

    @field:Element(name = "url_image")
    @param:Element(name = "url_image")
    var urlImage: String = ""
) : Nota(uuid, checkBox, fechaCreacion) {
    override fun toString(): String {
        return "NotaImagen(urlImage='$urlImage')"
    }
}
