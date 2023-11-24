package dev.ivanronco.primeraappaccesodatos.models.categoria

import com.squareup.moshi.Json
import dev.ivanronco.primeraappaccesodatos.models.TODO.IValorTODO
import dev.ivanronco.primeraappaccesodatos.models.TODO.ListadoTODO

@Json(name = "categoria")
data class Categoria(
    val nombre: String,
    val prioridad: Int,
    val notasValorTODO: List<IValorTODO<String>>,
    val notasSublistaTODO: List<IValorTODO<ListadoTODO>>
)
