package models

class NotaAudio(checkBox: Boolean,
                fechaCreacion: String,
                val audio: String
                ) : Nota(checkBox, fechaCreacion) {
    override fun toString(): String {
        return "NotaAudio(audio='$audio')"
    }
}