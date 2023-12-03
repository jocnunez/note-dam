package com.dam.ad.notedam.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "subListItem")
data class SublistItem(
    @field:JacksonXmlProperty(localName = "check")
    @param:JacksonXmlProperty(localName = "check")
    val check: Boolean,
    @field:JacksonXmlProperty(localName = "subListValue")
    @param:JacksonXmlProperty(localName = "subListValue")
    val subListValue: String
){

    fun toCsvRow(separator: Char, tail: Char? = null): String {
        return "$check$separator$subListValue${tail ?: ""}"
    }
}