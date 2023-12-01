package models

class NotaConSubnota(checkBox: Boolean,
                     fechaCreacion: String,
                     val subnota: Subnota,
                     ) : Nota(checkBox, fechaCreacion)
{
    override fun toString(): String {
        return "NotaConSubnota(subnota=$subnota)"
    }
}