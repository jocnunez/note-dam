package dev.ivanronco.primeraappaccesodatos.models.TODO

import com.squareup.moshi.Json
import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO
import java.time.LocalDateTime

@Json(name = "sublista_TODO")
data class SublistaTODO(
    override val fechaCreacion: LocalDateTime,
    override val isChecked: Boolean,
    override val valor: ListadoTODO,
    override val tipoTODO: TipoTODO = TipoTODO.SUBLISTA
): IValorTODO<ListadoTODO>
