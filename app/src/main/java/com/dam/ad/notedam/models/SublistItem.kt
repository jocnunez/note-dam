package com.dam.ad.notedam.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name="subListItem")
data class SublistItem(

    @field:Element( name ="checkSubList")
    @param:Element( name ="checkSubList")
    val check: Boolean,

    @field:Element( name ="valor")
    @param:Element( name ="valor")
    val valor: String){

    fun toCsvRow(separator: Char, tail: Char? = null): String {
        return "$check$separator$valor${tail ?: ""}"
    }
}