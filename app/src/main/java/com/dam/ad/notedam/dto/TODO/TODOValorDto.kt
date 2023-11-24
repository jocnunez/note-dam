package dev.ivanronco.primeraappaccesodatos.dto.TODO

import com.squareup.moshi.Json

@Json(name = "TODO_Valor")
class TODOValorDto(
    val fechaCreacion: String,
    val isChecked: String,
    val valor: String,
    val tipoTODO: String
)