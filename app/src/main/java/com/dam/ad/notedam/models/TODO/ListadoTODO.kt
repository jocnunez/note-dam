package dev.ivanronco.primeraappaccesodatos.models.TODO

import com.squareup.moshi.Json

@Json(name = "TODOs_basicos")
data class ListadoTODO(
    @Json(name = "TODOs_basicos")
    val listadoTODO: List<ITODO>
)
