package com.dam.ad.notedam.Storage.Categoria

import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Storage.IStorageLocal
import java.util.*

import android.util.Log
import com.dam.ad.notedam.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

class StorageCategoriaJson : IStorageLocal<Categoria> {

    private val gson = Gson()
    private val path = Utils.mainActivity!!.filesDir
    private val fileName = "Categorias.json"

    override fun loadAllItems(uuid: UUID): MutableList<Categoria> {
        val folder = File(Utils.mainActivity!!.filesDir, "NoteDam")

        // Crea el directorio si no existe
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                Log.i("Fichero", folder.absolutePath)
            } else {
                Log.e("Fichero", "Error al crear la carpeta")
            }
        }
        val file = File(folder, fileName)
        Log.i("StorageJson", "Leyendo JSON")

        if (!file.exists()) {
            Log.i("StorageJson", "Archivo no existe")
            return mutableListOf()
        }

        try {
            val json = file.readText()
            val type: Type = object : TypeToken<MutableList<Categoria>>() {}.type
            val listaCategorias: MutableList<Categoria> = gson.fromJson(json, type)

            var contador = 0
            listaCategorias.forEach {
                it.prioridadCategoria = contador
                contador += 1
            }

            listaCategorias.sortBy { it.prioridadCategoria }
            saveAllItems(UUID.randomUUID(), listaCategorias)

            return listaCategorias

        } catch (e: IOException) {
            Log.e("StorageCategoriaJson", "Error al leer el archivo JSON", e)
            return mutableListOf()
        }
    }

    override fun saveAllItems(uuid: UUID, listaItems: MutableList<Categoria>) {
        Log.i("StorageJson", "Writing All Items JSON")

        try {
            val folder = File(Utils.mainActivity!!.filesDir, "NoteDam")

            // Crea el directorio si no existe
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    Log.i("Fichero", folder.absolutePath)
                } else {
                    Log.e("Fichero", "Error al crear la carpeta")
                }
            }
            val file = File(folder, fileName)
            val json = gson.toJson(listaItems)
            file.writeText(json)
            Log.i("StorageJson", "Escritura exitosa en el archivo JSON")

        } catch (e: IOException) {
            Log.e("StorageCategoriaJson", "Error al escribir en el archivo JSON", e)
        }
    }
}
