package dev.ivanronco.primeraappaccesodatos.models.TODO

import java.time.LocalDateTime

interface ITODO {
    val fechaCreacion: LocalDateTime
    val isChecked: Boolean
}