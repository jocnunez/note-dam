package dev.ivanronco.primeraappaccesodatos.dto.TODO

import com.squareup.moshi.Json

@Json(name = "listado_TODO_valor")
class ListadoTODOValorDto(
    @Json(name = "listado_TODO_valor")
    val todos: List<TODOValorDto>
)