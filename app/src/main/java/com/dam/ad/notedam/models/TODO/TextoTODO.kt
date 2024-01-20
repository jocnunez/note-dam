package dev.ivanronco.primeraappaccesodatos.models.TODO

import com.squareup.moshi.Json
import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO
import java.time.LocalDateTime

@Json(name = "texto_TODO")
data class TextoTODO(
    override val fechaCreacion: LocalDateTime,
    override val isChecked: Boolean,
    override val valor: String,
    override val tipoTODO: TipoTODO = TipoTODO.TEXTO
): IValorTODO<String>