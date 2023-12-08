package com.dam.ad.notedam.Models.DTO

import com.dam.ad.notedam.Models.nota.SubList

data class SubListDto (
    var boolean : String? = null,
    var textoSublist : String? = null
) {

    fun toSubList(): SubList = SubList(
        boolean = boolean.toBoolean(), texto = textoSublist!!
    )


}