package dev.ivanronco.primeraappaccesodatos.dto.TODO

import com.squareup.moshi.Json

@Json(name = "TODO")
class TODODto(
    val fechaCreacion: String,
    val isChecked: String
)