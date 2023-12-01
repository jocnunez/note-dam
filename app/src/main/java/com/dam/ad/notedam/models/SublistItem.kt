package com.dam.ad.notedam.models

data class SublistItem(val check: Boolean, val valor: String){
    fun toCsvRow(separator: Char, tail: Char? = null): String {
        return "$check$separator$valor${tail ?: ""}"
    }
}