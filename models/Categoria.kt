package models

import java.util.*


class Categoria(
    val id: UUID = UUID.randomUUID(),
    val isChecked: Boolean,
    val prioridad: Int,
    val nombre: String,
    val notas: List<Nota>

) {

}