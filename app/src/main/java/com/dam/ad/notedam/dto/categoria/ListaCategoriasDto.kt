package dev.ivanronco.primeraappaccesodatos.dto.categoria

import com.squareup.moshi.Json

@Json(name = "categorias")
class ListaCategoriasDto(
    @Json(name = "categorias")
    val categorias: List<CategoriaDto>
)