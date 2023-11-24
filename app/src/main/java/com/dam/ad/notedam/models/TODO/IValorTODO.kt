package dev.ivanronco.primeraappaccesodatos.models.TODO

import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO

interface IValorTODO <T>: ITODO {
    val valor: T
    val tipoTODO: TipoTODO
}