package com.dam.ad.notedam.utils

import com.dam.ad.notedam.models.errors.CategoryError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.io.File

class Utils {
    companion object {
        /**
         * Elimina todos los ficheros que terminen por fileNameBase y
         * que no estén en la lista de ficheros que se están usando
         * @param filesUsing lista de ficheros que se están usando
         * @param filePath ruta donde se encuentran los ficheros
         * @param fileNameBase nombre base de los ficheros
         */
        fun clearFilesFn(filesUsing: Iterable<String>, filePath: String, fileNameBase: String): Result<Boolean, CategoryError> {
            val files = File(filePath).listFiles { _, name -> name.endsWith(fileNameBase) }
            try {
                files?.forEach { file ->
                    if(!filesUsing.contains(file.name) && file.exists() && !file.delete()){
                        return Err(CategoryError.RemoveFileError("JSON"))
                    }
                }
            }catch (e: Exception){
                return Err(CategoryError.RemoveFileError("JSON"))
            }
            return Ok(true)
        }
    }
}