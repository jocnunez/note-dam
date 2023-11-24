package dev.ivanronco.primeraappaccesodatos.dto.categoria

import com.squareup.moshi.Json
import dev.ivanronco.primeraappaccesodatos.dto.TODO.ListadoTODOSublistaDto
import dev.ivanronco.primeraappaccesodatos.dto.TODO.ListadoTODOValorDto

@Json(name = "categoria")
class CategoriaDto (
    val nombre: String,
    val prioridad: Int,
    val notasValorTODO: ListadoTODOValorDto,
    val notasSublistaTODO: ListadoTODOSublistaDto
)