package com.dam.ad.notedam.services.storage.categories

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.utils.mappers.fromCsvRowToCategory
import com.dam.ad.notedam.utils.mappers.toCsvRow
import com.dam.ad.notedam.utils.validators.FileAction
import com.dam.ad.notedam.utils.validators.validate
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import java.io.File

class CategoryStorageCsv: CategoryStorageService {
    private val fileName = "_categories.csv"
    private val header = "uuid,title,description,priority,notes\n"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun saveAll(elements: Iterable<Category>, filePath: String): Result<Iterable<Category>, CategoryError> {
        val file = File(filePath + fileName)
        return file.validate(FileAction.WRITE).mapBoth(
            success = {
                return try{
                    file.writeText(header)
                    elements.forEach {
                        file.appendText(it.toCsvRow())
                    }
                    Ok(elements)
                }catch (e: Exception){
                    Err(CategoryError.ExportError("CSV"))
                }
            },
            failure = {
                Err(CategoryError.ExportError("CSV"))
            }
        )
    }

    override fun loadAll(filePath: String): Result<List<Category>, CategoryError> {
        val file = File(filePath + fileName)
        return file.validate(FileAction.READ).mapBoth(
            success = {
                try {
                    Ok(file.readLines().drop(1).map { it.fromCsvRowToCategory() })
                } catch (e: Exception) {
                    Err(CategoryError.ImportError("CSV"))
                }
            },
            failure = {
                Err(CategoryError.ImportError("CSV"))
            }
        )
    }
}