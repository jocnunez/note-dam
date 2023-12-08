package com.dam.ad.notedam.Models.JsonAdapters

import android.net.Uri
import android.util.Log
import com.dam.ad.notedam.Enums.NotaType
import com.dam.ad.notedam.Models.nota.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

class NotaTypeAdapter : TypeAdapter<Nota>() {

    init {
        Log.i("NotaTypeAdapter", "ADAPTER CREADO")
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter?, nota: Nota?) {
        Log.i(this.javaClass.simpleName, "Write($nota)")
        out?.beginObject()

        // Verifica si nota es nula y toma acción apropiada
        if (nota == null) {
            throw IOException("La nota no puede ser nula")
        }

        when (nota) {
            is NotaTexto -> out?.name("tipoNota")?.value(NotaType.Texto.name)
            is NotaImagen -> out?.name("tipoNota")?.value(NotaType.Imagen.name)
            is NotaLista -> out?.name("tipoNota")?.value(NotaType.Lista.name)
        }

        // Ahora agrega el resto de las propiedades
        when (nota) {
            is NotaTexto -> {
                out?.name("textoNota")?.value(nota.textoNota)
                out?.name("uuid")?.value(nota.uuid.toString())
                out?.name("prioridad")?.value(nota.prioridad)
            }
            is NotaImagen -> {
                out?.name("textoNota")?.value(nota.textoNota)
                out?.name("uuid")?.value(nota.uuid.toString())
                out?.name("uriImagen")?.value(nota.uriImagen.toString())
                out?.name("prioridad")?.value(nota.prioridad)
            }
            is NotaLista -> {
                out?.name("textoNota")?.value(nota.textoNota)
                out?.name("uuid")?.value(nota.uuid.toString())
                out?.name("lista")?.beginArray()
                nota.lista.forEach { sublista ->
                    out?.beginObject()
                    out?.name("textoSublista")?.value(sublista.texto)
                    out?.name("booleanSublista")?.value(sublista.boolean)
                    out?.endObject()
                }
                out?.endArray()
                out?.name("prioridad")?.value(nota.prioridad)
            }
        }

        out?.endObject()
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Nota {
        reader.beginObject() // Comenzar a leer el objeto

        var tipoNota: NotaType? = null
        var textoNota: String? = null
        var uuid: String? = null
        var prioridad: Int = 0
        var lista: MutableList<SubList>? = null
        var imagenUri : String? = null

        while (reader.hasNext()) {
            val name = reader.nextName()
            when (name) {
                "tipoNota" -> {
                    tipoNota = NotaType.valueOf(reader.nextString())
                }
                "textoNota" -> {
                    textoNota = reader.nextString()
                }
                "uuid" -> {
                    uuid = reader.nextString()
                }
                "prioridad" -> {
                    prioridad = reader.nextInt()
                }
                "uriImagen" -> {
                    // Este campo no se almacena en el objeto NotaImagen y se ignora
                    imagenUri = reader.nextString()
                }
                "lista" -> {
                    // Deserializar la lista de sublistas desde el campo "lista" en el JSON
                    lista = readSublistArray(reader)
                }
                else -> {
                    reader.skipValue() // Ignorar valores no deseados
                }
            }
        }

        reader.endObject() // Finalizar la lectura del objeto

        return when (tipoNota) {
            NotaType.Texto -> NotaTexto(textoNota = textoNota ?: "", uuid = UUID.fromString(uuid), prioridad = prioridad)
            NotaType.Imagen -> NotaImagen(textoNota = textoNota ?: "", uuid = UUID.fromString(uuid ), prioridad = prioridad, uriImagen = Uri.parse(imagenUri ?: ""))
            NotaType.Lista -> NotaLista(textoNota = textoNota ?: "", uuid = UUID.fromString(uuid), prioridad = prioridad, lista = lista ?: mutableListOf())
            else -> throw IllegalArgumentException("Tipo de nota desconocido: $tipoNota")
        }
    }

    private fun readSublistArray(reader: JsonReader): MutableList<SubList> {
        val sublistArray = mutableListOf<SubList>()

        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            var text = ""
            var checked = false

            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "textoSublista" -> text = reader.nextString()
                    "booleanSublista" -> checked = reader.nextBoolean()
                    else -> reader.skipValue()
                }
            }

            reader.endObject()
            sublistArray.add(SubList(texto = text, boolean =  checked))
        }
        reader.endArray()

        return sublistArray
    }
}