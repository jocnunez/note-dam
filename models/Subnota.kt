package models

class Subnota(checkBox: Boolean, fechaCreacion: String) : Nota(checkBox, fechaCreacion) {

    override fun toString(): String {
        return "checkbox: $checkBox, fechaCreacion: $fechaCreacion "
    }
}
