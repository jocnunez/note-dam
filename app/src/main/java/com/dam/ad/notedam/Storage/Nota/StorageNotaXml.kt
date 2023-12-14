package com.dam.ad.notedam.Storage.Nota

import android.util.Log
import com.dam.ad.notedam.Models.DTO.NotaDto
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.Storage.IStorageLocal
import com.dam.ad.notedam.utils.Utils
import com.dam.ad.notedam.utils.toDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File
import java.util.*

class StorageNotaXml : IStorageLocal<Nota> {
    private val path = Utils.mainActivity!!.getExternalFilesDir(null)

    override fun loadAllItems(uuid: UUID): MutableList<Nota> {
        val folder = File(Utils.mainActivity!!.filesDir, "Notas")
        val file = File(folder, "$uuid.xml")

        val mapper = XmlMapper()
        val typeReference: TypeReference<List<NotaDto>> = object : TypeReference<List<NotaDto>>() {}

        return try {
            if (!file.exists()) {
                mutableListOf()
            } else {
                val listaNotasDto: List<NotaDto> = mapper.readValue(file, typeReference)
                listaNotasDto.map { it.toNota() }.toMutableList()
            }
        } catch (e: Exception) {
            Log.e("StorageXml", "Error al leer el archivo XML", e)
            mutableListOf()
        }
    }

    override fun saveAllItems(uuid: UUID, listaItems: MutableList<Nota>) {
        val folder = File(Utils.mainActivity!!.filesDir, "Notas")
        val file = File(folder, "$uuid.xml")

        val mapper = XmlMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Log.i("StorageXml", "Archivo creado con éxito: ${file.absolutePath}")
                } else {
                    Log.e("StorageXml", "Error al crear el archivo")
                    return
                }
            }

            // Convierte la lista de Nota a lista de NotaDto antes de escribir en el archivo
            val listaDto = listaItems.toDto()
            mapper.writeValue(file, listaDto)
        } catch (e: Exception) {
            Log.e("StorageXml", "Error al escribir en el archivo XML", e)
        }
    }

    fun deleteAllTipo(path: String,tipoFichero:String){
        val files = File(path).listFiles { _, name -> name.endsWith(tipoFichero) }
        files.forEach { it.delete() }
    }


}