package com.dam.ad.notedam.Storage

import android.util.Log
import com.dam.ad.notedam.utils.Utils
import java.io.File
import java.util.*

interface IStorage <T>{
    fun loadAllItems(uuid : UUID) : MutableList<T>
    fun saveAllItems(path : UUID, listaItems : MutableList<T>)
    fun deleteFilesWithUUID(uuid: UUID) {
        val folder = File(Utils.mainActivity!!.filesDir, "Notas")

        if (folder.exists() && folder.isDirectory) {
            val filesToDelete = folder.listFiles { file ->
                file.isFile && file.name.startsWith(uuid.toString())
            }

            filesToDelete?.forEach { file ->
                if (file.delete()) {
                    Log.i("DeleteFiles", "Archivo eliminado con éxito: ${file.absolutePath}")
                } else {
                    Log.e("DeleteFiles", "Error al eliminar el archivo: ${file.absolutePath}")
                }
            }
        }
    }
}