package com.dam.ad.notedam.repositories

import android.util.Log
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Config.ConfigFileType
import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Enums.FileType
import com.dam.ad.notedam.Enums.StorageType
import com.dam.ad.notedam.Models.Categoria
import com.dam.ad.notedam.Storage.Categoria.*
import com.dam.ad.notedam.Storage.IStorage
import com.dam.ad.notedam.errors.CategoriaAlreadyExistsError
import com.dam.ad.notedam.errors.CategoriaError
import com.dam.ad.notedam.errors.CategoriaFileEmptyError
import com.dam.ad.notedam.errors.CategoriaNotFoundError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import java.util.*

class CategoriaRepository (activity: MainActivity) : ICategoriaRepository {
    var storage: IStorage<Categoria>
    private var listaCategorias: MutableList<Categoria>

    init {
        storage = when (ConfigStorageType.loadStorageType(activity)) {
            StorageType.Local -> {
                when (ConfigFileType.loadFileType(activity)) {
                    FileType.CSV -> StorageCategoriaCsv()
                    FileType.JSON -> StorageCategoriaJson()
                    FileType.XML -> StorageCategoriaXml()
                }
            }

            StorageType.Sql -> StorageCategoriaSql()
            StorageType.MongoDB -> StorageCategoriaMongo()
        }

        listaCategorias = storage.loadAllItems(UUID.randomUUID()) //No es necesaria aqui la id

        listaCategorias
    }

    override fun addItem(item: Categoria): Result<Categoria, CategoriaError> {
        Log.i("CategoriaRepository", "addItem ($item)")
        listaCategorias.find { it.uuid == item.uuid }?.let {
            return Err(CategoriaAlreadyExistsError("Ya existe una categoria con este error"))
        } ?: let {
            listaCategorias.add(item)
            storage.saveAllItems(UUID.randomUUID(), listaCategorias)
            return Ok(item)
        }
    }

    override fun deleteItem(item: Categoria): Result<Categoria, CategoriaError> {
        val categoriaEncontrada = listaCategorias.find { it.uuid == item.uuid }

        return if (categoriaEncontrada != null) {
            listaCategorias.remove(categoriaEncontrada)
            storage.saveAllItems(UUID.randomUUID(), listaCategorias)
            storage.deleteFilesWithUUID(item.uuid)
            Ok(categoriaEncontrada)
        } else {
            // La categoría no existe
            Err(CategoriaNotFoundError("No existe una categoría con esta UUID"))
        }
    }

    override fun loadAllItems(): Result<MutableList<Categoria>, CategoriaError> {
        listaCategorias = storage.loadAllItems(UUID.randomUUID())
        return Ok(listaCategorias)
    }

    override fun updateItem(item: Categoria): Result<Categoria, CategoriaError> {
        listaCategorias.find { it.uuid == item.uuid }
            ?.let {
                it.copy(
                    uuid = item.uuid,
                    nombreCategoria = item.nombreCategoria,
                    prioridadCategoria = item.prioridadCategoria
                )
                storage.saveAllItems(UUID.randomUUID(), listaCategorias)
            }?: let {
                return Err(CategoriaNotFoundError("La categoria $item no existe"))
            }
        return Ok(item)
    }

    override fun addAllItems(items: MutableList<Categoria>): Result<MutableList<Categoria>, CategoriaError> {
        items.forEach {
            addItem(it).onFailure { error ->
                return Err(error)
            }
        }
        storage.saveAllItems(UUID.randomUUID(), listaCategorias)
        return Ok(items)
    }




}