package com.dam.ad.notedam.Storage.Categoria

import android.util.Log
import com.dam.ad.notedam.utils.MainContext
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Storage.IStorageLocal
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import java.io.File
import java.io.IOException
import java.util.*

class StorageCategoriaXml : IStorageLocal<Categoria> {

    private val xmlMapper = XmlMapper().apply {
        enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
        enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT)
    }

    private val path = MainContext.mainActivity!!.filesDir
    private val fileName = "Categorias.xml"

    override fun loadAllItems(uuid: UUID): MutableList<Categoria> {
        val folder = File(MainContext.mainActivity!!.filesDir, "NoteDam")

        // Crea el directorio si no existe
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                Log.i("Fichero", folder.absolutePath)
            } else {
                Log.e("Fichero", "Error al crear la carpeta")
            }
        }

        val file = File(folder, fileName)
        Log.i("StorageXml", "Leyendo XML")

        if (!file.exists()) {
            Log.i("StorageXml", "Archivo no existe")
            return mutableListOf()
        }

        try {
            val listaCategorias: MutableList<Categoria> = xmlMapper.readValue(
                file,
                xmlMapper.typeFactory.constructCollectionType(MutableList::class.java, Categoria::class.java)
            )

            var contador = 0
            listaCategorias.forEach {
                it.prioridadCategoria = contador
                contador += 1
            }

            listaCategorias.sortBy { it.prioridadCategoria }
            saveAllItems(UUID.randomUUID(), listaCategorias)

            return listaCategorias

        } catch (e: IOException) {
            Log.e("StorageCategoriaXml", "Error al leer el archivo XML", e)
            return mutableListOf()
        }
    }

    override fun saveAllItems(uuid: UUID, listaItems: MutableList<Categoria>) {
        Log.i("StorageXml", "Writing All Items XML")

        try {
            val folder = File(MainContext.mainActivity!!.filesDir, "NoteDam")

            // Crea el directorio si no existe
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    Log.i("Fichero", folder.absolutePath)
                } else {
                    Log.e("Fichero", "Error al crear la carpeta")
                }
            }

            val file = File(folder, fileName)
            xmlMapper.writeValue(file, listaItems)

            Log.i("StorageXml", "Escritura exitosa en el archivo XML")

        } catch (e: IOException) {
            Log.e("StorageCategoriaXml", "Error al escribir en el archivo XML", e)
        }
    }
}