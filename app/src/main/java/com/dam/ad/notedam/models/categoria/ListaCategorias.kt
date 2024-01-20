package dev.ivanronco.primeraappaccesodatos.models.categoria

import com.squareup.moshi.Json

@Json(name = "categorias")
data class ListaCategorias(
    @Json(name = "categorias")
    val categorias: List<Categoria>
)