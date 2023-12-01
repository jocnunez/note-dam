package models

abstract class Nota(
    val checkBox: Boolean,
    val fechaCreacion: String
) {
    override fun toString(): String {
        return "Nota(checkBox=$checkBox, fechaCreacion='$fechaCreacion')"
    }
}