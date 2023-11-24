package dev.ivanronco.primeraappaccesodatos.dto.TODO

import com.squareup.moshi.Json

@Json(name = "listado_TODO_sublista")
class ListadoTODOSublistaDto(
    @Json(name = "listado_TODO_sublista")
    val todos: List<TODOSublistaDto>
)