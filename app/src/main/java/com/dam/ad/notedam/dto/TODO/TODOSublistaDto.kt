package dev.ivanronco.primeraappaccesodatos.dto.TODO

import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO

class TODOSublistaDto(
    val fechaCreacion: String,
    val isChecked: String,
    val valor: ListadoTODODto,
    val tipoTODO: String = TipoTODO.SUBLISTA.texto
)