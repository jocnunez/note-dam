package com.dam.ad.notedam.Storage.Nota

import com.dam.ad.notedam.Config.ConfigStorageType
import com.dam.ad.notedam.Enums.StorageType
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.Storage.IStorage
import com.dam.ad.notedam.utils.Utils
import java.util.*

class StorageNotaMongo : IStorage<Nota> {
    override fun loadAllItems(uuid: UUID): MutableList<Nota> {
        //TODO:
        ConfigStorageType.saveStorageType(Utils.mainActivity!!, StorageType.Local)
        TODO("Sin implementar")
    }

    override fun saveAllItems(path: UUID, listaItems: MutableList<Nota>) {
        //TODO:
        ConfigStorageType.saveStorageType(Utils.mainActivity!!, StorageType.Local)
        TODO("Sin implementar")
    }
}