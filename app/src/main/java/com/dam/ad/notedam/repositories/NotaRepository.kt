package com.dam.ad.notedam.repositories

import StorageNotaJson
import android.util.Log
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Enums.FileType
import com.dam.ad.notedam.Enums.StorageType
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.Models.nota.NotaImagen
import com.dam.ad.notedam.Models.nota.NotaLista
import com.dam.ad.notedam.Models.nota.NotaTexto
import com.dam.ad.notedam.Storage.IStorage
import com.dam.ad.notedam.Storage.Nota.StorageNotaCsv
import com.dam.ad.notedam.Storage.Nota.StorageNotaMongo
import com.dam.ad.notedam.Storage.Nota.StorageNotaSql
import com.dam.ad.notedam.Storage.Nota.StorageNotaXml
import com.dam.ad.notedam.errors.NotaAlreadyExistsError
import com.dam.ad.notedam.errors.NotaError
import com.dam.ad.notedam.errors.NotaNotFoundError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.*

class NotaRepository (val activity: MainActivity , val uuidCategoria : UUID, ) : INotaRepository {

    lateinit var storage: IStorage<Nota>
    lateinit var listaNotas: MutableList<Nota>

    init {
        storage = when (ConfigStorageType.loadStorageType(activity)) {
            StorageType.Local -> {
                when (ConfigFileType.loadFileType(activity)) {
                    FileType.CSV -> StorageNotaCsv()
                    FileType.JSON -> StorageNotaJson()
                    FileType.XML -> StorageNotaXml()
                }
            }

            StorageType.Sql -> StorageNotaSql()
            StorageType.MongoDB -> StorageNotaMongo()
        }

        listaNotas = storage.loadAllItems(uuid = uuidCategoria)

    }

    override fun addItem(item: Nota): Result<Nota, NotaError> {
        Log.i("NotaRepository", "addItem ($item)")
        listaNotas.find { it.uuid == item.uuid }?.let {
            return Err(NotaAlreadyExistsError("Ya existe una nota con esta uuid"))
        } ?: let {
            listaNotas.add(item)
            storage.saveAllItems(uuidCategoria, listaNotas)
            return Ok(item)
        }
    }

    override fun deleteItem(item: Nota): Result<Nota, NotaError> {
        val notaEncontrada = listaNotas.find { it.uuid == item.uuid  }

        return if (notaEncontrada != null) {
            listaNotas.removeIf { notaEncontrada.uuid == it.uuid }
            storage.saveAllItems(uuidCategoria, listaNotas)
            Ok(notaEncontrada)
        } else {
            // La categoría no existe
            Err(NotaNotFoundError("No existe una nota con esta UUID"))
        }
    }

    override fun loadAllItems(): Result<MutableList<Nota>, NotaError> {
        listaNotas = storage.loadAllItems(uuidCategoria)
        return Ok(listaNotas)
    }

    override fun updateItem(item: Nota): Result<Nota, NotaError> {
        listaNotas.find { it.uuid == item.uuid }
            ?.let {
                when (it) {
                    is NotaTexto -> {
                        it.copy(
                            textoNota = (item as NotaTexto).textoNota
                        )
                    }
                    is NotaImagen -> {
                        it.copy(
                            textoNota = (item as NotaImagen).textoNota,
                            uriImagen = item.uriImagen
                        )
                    }
                    is NotaLista -> {
                        it.copy(
                            textoNota = (item as NotaLista).textoNota,
                            lista = item.lista
                        )
                    }
                }
                storage.saveAllItems(uuidCategoria, listaNotas)
            }?: let {
            return Err(NotaNotFoundError("La nota $item no existe"))
        }
        return Ok(item)
    }

    override fun addAllItems(items: MutableList<Nota>): Result<MutableList<Nota>, NotaError> {
        items.forEach {
            listaNotas.add(it)
        }
        storage.saveAllItems(uuidCategoria, listaNotas)
        return Ok(items)
    }
}