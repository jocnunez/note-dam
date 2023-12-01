package models

class NotaText(checkBox: Boolean,
               fechaCreacion: String,
               val texto:String) : Nota(checkBox, fechaCreacion) {
    override fun toString(): String {
        return "NotaText(texto='$texto')"
    }
}