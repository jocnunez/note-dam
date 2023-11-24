package dev.ivanronco.primeraappaccesodatos.models.TODO

import com.squareup.moshi.Json
import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO
import java.time.LocalDateTime

@Json(name = "audio_TODO")
data class AudioTODO(
    override val fechaCreacion: LocalDateTime,
    override val isChecked: Boolean,
    override val valor: String,
    override val tipoTODO: TipoTODO = TipoTODO.AUDIO
): IValorTODO<String>
