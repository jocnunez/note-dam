package com.dam.ad.notedam.Storage.Nota

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.dam.ad.notedam.Models.nota.*
import com.dam.ad.notedam.Storage.IStorageLocal
import com.dam.ad.notedam.utils.MainContext
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

class StorageNotaCsv : IStorageLocal<Nota> {
    val path = MainContext.mainActivity!!.getExternalFilesDir(null)
    override fun loadAllItems(uuid: UUID): MutableList<Nota> {
        val folder = File(MainContext.mainActivity!!.filesDir, "Notas")

        if (!folder.exists() && !folder.mkdirs()) {
            Log.e("Fichero", "Error al crear la carpeta")
            return mutableListOf()
        }

        val file = File(folder, "$uuid.csv")

        Log.i("StorageCsv", "Leyendo CSV")
        val listaNotas = mutableListOf<Nota>()
        Log.i("StorageCategoriaCSV", "Ruta del archivo: ${file.absolutePath}")

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Log.i("StorageCategoriaCSV", "Archivo creado con éxito: ${file.absolutePath}")
                } else {
                    Log.e("StorageCategoriaCSV", "Error al crear el archivo")
                    return mutableListOf()
                }
            }

            BufferedReader(FileReader(file)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val columns = line!!.split(",")

                    if (columns.size >= 4) {
                        val tipoNota = columns[0]
                        val textoNota = columns[1].replace("\\n", "\n")
                        val uuidNota = UUID.fromString(columns[2])
                        val prioridadNota = columns[3].toInt()

                        val listaElementos = mutableListOf<SubList>()

                        if (columns.size > 4 && tipoNota == "Lista") {
                            // La información de la lista está en columns[4]
                            val elementos = columns[4].split("::")
                            for (elemento in elementos) {
                                val subListaElementos = elemento.split("|")
                                if (subListaElementos.size == 2) {
                                    val textoElemento = subListaElementos[0].replace("\\n", "\n")
                                    val booleanElemento = subListaElementos[1].toBoolean()
                                    listaElementos.add(SubList(texto = textoElemento, boolean = booleanElemento))
                                }
                            }
                        }

                        val nota: Nota = when (tipoNota) {
                            "Texto" -> NotaTexto(textoNota = textoNota, uuid = uuidNota, prioridadNota)
                            "Imagen" -> {
                                val uriImagen = Uri.parse(columns[4])
                                NotaImagen(textoNota = textoNota, uuid = uuidNota, prioridad = prioridadNota, uriImagen = uriImagen)
                            }
                            "Lista" -> NotaLista(textoNota = textoNota, uuid = uuidNota, prioridad = prioridadNota, lista = listaElementos)

                            else -> throw IllegalArgumentException("Tipo de nota desconocido: $tipoNota")
                        }

                        listaNotas.add(nota)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("StorageCategoriaCSV", "Error al leer el archivo", e)
        }



        listaNotas.apply { this.sortBy { it.prioridad } }
        var contador = 0
        listaNotas.forEach {
            it.prioridad = contador
            contador+=1
        }

        return listaNotas
    }



    override fun saveAllItems(uuid: UUID, listaItems: MutableList<Nota>) {
        Log.i("StorageCsv", "Writing All Items CSV ")
        val folder = File(MainContext.mainActivity!!.filesDir, "Notas")
        val fichero = File(folder, "$uuid.csv")
        fichero.writeText("")
        listaItems.forEach {
            writeCSV(uuid, it)
        }
    }

    private fun writeCSV(uuid: UUID, nota: Nota) {
        Log.i("StorageNotaCsv", "Writing CSV -> $nota")
        val folder = File(MainContext.mainActivity!!.filesDir, "Notas")
        val fichero = File(folder, "$uuid.csv")
        Log.i("StorageNotaCsv", " UNOOOOO")
        // si no existe lo creamos
        if (!fichero.exists()) {
            fichero.createNewFile()
        }

        val textoNota = when (nota) {
            is NotaTexto -> nota.textoNota.replace("\n", "\\n")
            is NotaImagen -> nota.textoNota.replace("\n", "\\n")
            is NotaLista -> nota.textoNota.replace("\n", "\\n")
        }

        when (nota) {
            is NotaTexto -> fichero.appendText("${nota.tipoNota},${textoNota},${nota.uuid},${nota.prioridad}${System.lineSeparator()}")
            is NotaImagen -> fichero.appendText("${nota.tipoNota},${textoNota},${nota.uuid},${nota.prioridad},${nota.uriImagen}${System.lineSeparator()}")
            is NotaLista -> {
                Log.d("CSV Save", "Guardando nota tipo Lista")
                fichero.appendText("${nota.tipoNota},$textoNota,${nota.uuid},${nota.prioridad},")

                if (nota.lista.isNotEmpty()) {
                    val elementosLista = nota.lista.joinToString("::") { "${it.texto}|${it.boolean}" }
                    fichero.appendText(elementosLista)
                }

                fichero.appendText(System.lineSeparator())
                Log.d("CSV Save", "Guardado completado")
            }
        }
    }

}