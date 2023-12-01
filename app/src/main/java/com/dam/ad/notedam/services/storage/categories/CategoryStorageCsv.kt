package com.dam.ad.notedam.services.storage.categories

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.utils.Utils
import com.dam.ad.notedam.utils.mappers.fromCsvRowToCategory
import com.dam.ad.notedam.utils.mappers.toCsvRow
import com.dam.ad.notedam.utils.validators.FileAction
import com.dam.ad.notedam.utils.validators.validate
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class CategoryStorageCsv: CategoryStorageService {
    private val fileNameBase = "_categories.csv"
    private val header = "uuid,title,description,priority,notes\n"

    override fun export(element: Category, filePath: String, clearFiles: Boolean): Result<Category, CategoryError> {
        val fileUsing = filePath + element.uuid + fileNameBase
        if(clearFiles){
            Utils.clearFilesFn(listOf(fileUsing), filePath, fileNameBase)
        }
        val file = File(filePath + element.uuid + fileNameBase)
        return file.validate(FileAction.WRITE).mapBoth(
            success = {
                try{
                    file.writeText(header)
                    file.appendText(element.toCsvRow())
                    Ok(element)
                }catch (e: Exception){
                    Err(CategoryError.ExportError("CSV"))
                }
            },
            failure = {
                Err(CategoryError.ExportError("CSV"))
            }
        )
    }

    override fun exportAll(elements: Iterable<Category>, filePath: String, clearFiles: Boolean): Result<Iterable<Category>, CategoryError> {
        val filesUsing = elements.map { filePath + it.uuid + fileNameBase }
        if(clearFiles){
            Utils.clearFilesFn(filesUsing, filePath, fileNameBase)
        }

        elements.forEach {category ->
            val file = File(filePath + category.uuid + fileNameBase)
            file.validate(FileAction.WRITE).mapBoth(
                success = {
                    try{
                        file.writeText(header)
                        file.appendText(category.toCsvRow())
                    }catch (e: Exception){
                        return Err(CategoryError.ExportError("CSV"))
                    }
                },
                failure = {
                    return Err(CategoryError.ExportError("CSV"))
                }
            )
        }

        return Ok(elements)
    }

    override fun loadAll(filePath: String): Result<List<Category>, CategoryError> {
        //Todos los ficheros que terminen en _categories.csv
        val files = File(filePath).listFiles { _, name -> name.endsWith(fileNameBase) }
        if(files.isNullOrEmpty()){
            return Err(CategoryError.ImportError("JSON"))
        }
        val categories = mutableListOf<Category>()
        files.forEach { file ->
            file.validate(FileAction.READ).mapBoth(
                success = {
                    try{
                        categories.addAll(file.readLines().drop(1).map { it.fromCsvRowToCategory() })
                    }catch (e: Exception){
                        return Err(CategoryError.ImportError("CSV"))
                    }
                },
                failure = {
                    return Err(CategoryError.ImportError("CSV"))
                }
            )
        }
        return Ok(categories)
    }
}