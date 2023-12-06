package com.dam.ad.notedam.Models

import java.util.UUID

data class Categoria (
    val uuid : UUID = UUID.randomUUID(),
    var nombreCategoria : String
) {
}