package com.dam.ad.notedam.Storage.Categoria

import android.util.Log
import com.dam.ad.notedam.utils.MainContext
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Storage.IStorageLocal
import java.io.File
import java.io.IOException
import java.util.*

class StorageCategoriaCsv : IStorageLocal<Categoria> {
    val path = MainContext.mainActivity!!.getExternalFilesDir(null)
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

        val file = File(folder, "Categorias.csv")

        Log.i("StorageCsv", "Leyendo CSV")
        val listaCategorias = mutableListOf<Categoria>()
        Log.i("StorageCategoriaCSV", "Ruta del archivo: ${file.absolutePath}")

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    // El archivo se creó con éxito
                    Log.i("StorageCategoriaCSV", "Archivo creado con éxito: ${file.absolutePath}")
                } else {
                    // Fallo al crear el archivo
                    Log.i("StorageCategoriaCSV", "Error al crear el archivo")
                }
            }

            val bufferedReader = file.bufferedReader()
            val lineas = bufferedReader.readLines()

            // Procesar cada línea según sea necesario
            for (linea in lineas) {
                val columnas = linea.split(",")
                val nombreCategoria = columnas[0].replace("\\n", "\n")
                listaCategorias.add(
                    Categoria(
                        nombreCategoria = nombreCategoria,
                        prioridadCategoria = columnas[1].toInt(),
                        uuid = UUID.fromString(columnas[2])!!
                    )
                )
            }

            bufferedReader.close()

        } catch (e: IOException) {
            // Manejar la excepción al leer el archivo
            Log.e("StorageCategoriaCSV", "Error al leer el archivo", e)
        }
        var contador = 0
        listaCategorias.forEach {
            Log.i("PRUEBA", it.toString())
            it.prioridadCategoria = contador
            contador += 1
        }

        listaCategorias.apply { sortBy { it.prioridadCategoria } }
        saveAllItems(UUID.randomUUID(), listaCategorias)
        return listaCategorias
    }


    override fun saveAllItems(uuid: UUID, listaItems: MutableList<Categoria>) {
        Log.i("StorageCsv", "Writing All Items CSV ")
        val folder = File(MainContext.mainActivity!!.filesDir, "NoteDam")
        val fichero = File(folder, "Categorias.csv")
        fichero.writeText("")
        listaItems.forEach {
            writeCSV(uuid, it)
        }
    }

    fun writeCSV(uuid: UUID, categoria: Categoria) {
        Log.i("StorageCsv", "Writing CSV -> $categoria")
        val folder = File(MainContext.mainActivity!!.filesDir, "NoteDam")
        val fichero = File(folder, "Categorias.csv")

        // si no existe lo creamos
        if (!fichero.exists()) {
            fichero.createNewFile()
        }

        // Reemplazamos los saltos de línea en el campo de texto con un carácter de escape
        val textoCategoria = categoria.nombreCategoria.replace("\n", "\\n")
        // Escribimos el encabezado, separados por comas y seguido por el separador de línea del sistema
        fichero.appendText("${textoCategoria},${categoria.prioridadCategoria},${categoria.uuid}${System.lineSeparator()}")
        Log.i("StorageCsv", "Escritura exitosa en el archivo")
    }

}