package dev.ivanronco.primeraappaccesodatos.models.TipoTODO

import com.squareup.moshi.Json

@Json(name = "tipo_TODO")
enum class TipoTODO(val texto: String) {
    TEXTO("texto"), AUDIO("audio"), IMAGEN("imagen"), SUBLISTA("sublista")
}
