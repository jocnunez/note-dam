package com.dam.ad.notedam.utils

import com.dam.ad.notedam.Models.DTO.NotaDto
import com.dam.ad.notedam.Models.DTO.SubListDto
import com.dam.ad.notedam.Models.nota.*

fun MutableList<SubListDto>.listToSubList () : MutableList<SubList> {
    val lista = mutableListOf<SubList>()
    this.forEach {
        lista.add(it.toSubList())
    }
    return lista
}

fun MutableList<Nota>.toDto() : MutableList<NotaDto> {
    val lista = mutableListOf<NotaDto>()
    this.forEach {
        lista.add(it.toDto())
    }
    return lista
}

private fun Nota.toDto(): NotaDto {
    return when (this) {
        is NotaTexto -> generarNotaDtoTexto(this)
        is NotaImagen -> generarNotaDtoImagen(this)
        is NotaLista -> generarNotaDtoLista(this)
    }
}

private fun generarNotaDtoTexto(nota: NotaTexto) : NotaDto = NotaDto(
    tipoNota = nota.tipoNota.toString(),
    uuid = nota.uuid.toString(),
    textoNota = nota.textoNota,
    prioridad = nota.prioridad.toString(),
)

private fun generarNotaDtoImagen(nota: NotaImagen): NotaDto = NotaDto(
    tipoNota = nota.tipoNota.toString(),
    uuid = nota.uuid.toString(),
    textoNota = nota.textoNota,
    prioridad = nota.prioridad.toString(),
    uriImagen = nota.uriImagen.toString()
)

private fun generarNotaDtoLista(nota: NotaLista) : NotaDto = NotaDto(
    tipoNota = nota.tipoNota.toString(),
    uuid = nota.uuid.toString(),
    textoNota = nota.textoNota,
    prioridad = nota.prioridad.toString(),
    lista = nota.lista.map { it.toDto() }.toMutableList()
)

private fun SubList.toDto() : SubListDto = SubListDto(
    boolean = this.boolean.toString(),
    textoSublist = this.texto
)