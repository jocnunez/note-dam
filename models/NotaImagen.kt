package models

class NotaImagen(checkBox: Boolean,
                 fechaCreacion: String,
                 val urlImage: String
                 ) : Nota(checkBox, fechaCreacion) {
    override fun toString(): String {
        return "NotaImagen(urlImage='$urlImage')"
    }
}