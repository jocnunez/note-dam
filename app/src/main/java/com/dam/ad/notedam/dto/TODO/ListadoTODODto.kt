package dev.ivanronco.primeraappaccesodatos.dto.TODO

import com.squareup.moshi.Json

@Json(name = "listado_TODO")
class ListadoTODODto(
    @Json(name = "listado_TODO")
    val todos: List<TODODto>
)