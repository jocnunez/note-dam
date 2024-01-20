package dev.ivanronco.primeraappaccesodatos.models.TODO

import com.squareup.moshi.Json
import java.time.LocalDateTime

@Json(name = "TODO")
data class TODO(
    override val fechaCreacion: LocalDateTime,
    override val isChecked: Boolean
): ITODO
