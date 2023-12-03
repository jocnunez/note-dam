package com.dam.ad.notedam.services.storage.categories

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.dto.CategoryDto
import com.dam.ad.notedam.models.dto.NoteDto
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.models.gson_adapters.CategoryDtoAdapterGson
import com.dam.ad.notedam.models.gson_adapters.NoteDtoAdapterGson
import com.dam.ad.notedam.utils.Utils
import com.dam.ad.notedam.utils.mappers.toCategory
import com.dam.ad.notedam.utils.mappers.toCategoryDto
import com.dam.ad.notedam.utils.validators.FileAction
import com.dam.ad.notedam.utils.validators.validate
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.google.gson.GsonBuilder
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class CategoryStorageJson: CategoryStorageService{
    private val fileNameBase = "_categories.json"
    override fun export(element: Category, filePath: String, clearFiles: Boolean): Result<Category, CategoryError> {
        val fileUsing = filePath + element.uuid + fileNameBase
        if(clearFiles){
            Utils.clearFilesFn(listOf(fileUsing), filePath, fileNameBase)
        }
        val file = File(fileUsing)
        return file.validate(FileAction.WRITE).mapBoth(
            success = {
                try{
                    val gson = GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(NoteDto::class.java, NoteDtoAdapterGson())
                        .registerTypeAdapter(CategoryDto::class.java, CategoryDtoAdapterGson())
                        .create()
                    val jsonString = gson.toJson(element.toCategoryDto())
                    file.writeText(jsonString)
                    Ok(element)
                }catch (e: Exception){
                    Err(CategoryError.ExportError("JSON"))
                }
            },
            failure = {
                Err(CategoryError.ExportError("JSON"))
            }
        )
    }

    override fun exportAll(elements: Iterable<Category>, filePath: String, clearFiles: Boolean): Result<Iterable<Category>, CategoryError> {
        val filesUsing = elements.map { filePath + it.uuid + fileNameBase }
        if(clearFiles){
            Utils.clearFilesFn(filesUsing, filePath, fileNameBase)
        }

        elements.forEach { category ->
            val file = File(filePath + category.uuid + fileNameBase)
            file.validate(FileAction.WRITE).mapBoth(
                success = {
                    try{
                        val gson = GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(NoteDto::class.java, NoteDtoAdapterGson())
                            .registerTypeAdapter(CategoryDto::class.java, CategoryDtoAdapterGson())
                            .create()
                        val jsonString = gson.toJson(category.toCategoryDto())
                        file.writeText(jsonString)
                    }catch (e: Exception){
                        return Err(CategoryError.ExportError("JSON"))
                    }
                },
                failure = {
                    return Err(CategoryError.ExportError("JSON"))
                }
            )
        }

        return Ok(elements)
    }

    override fun loadAll(filePath: String): Result<List<Category>, CategoryError> {
        //Todos los ficheros que terminen en _categories.json
        val files = File(filePath).listFiles { _, name -> name.endsWith(fileNameBase) }
        if(files.isNullOrEmpty()){
            return Err(CategoryError.ImportError("JSON"))
        }
        val categories = mutableListOf<CategoryDto>()
        files.forEach { file ->
            file.validate(FileAction.READ).mapBoth(
                success = {
                    try{
                        val gson = GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(NoteDto::class.java, NoteDtoAdapterGson())
                            .registerTypeAdapter(CategoryDto::class.java, CategoryDtoAdapterGson())
                            .create()
                        val jsonString = file.readText()
                        categories.add(gson.fromJson(jsonString, CategoryDto::class.java))
                    }catch (e: Exception){
                        return Err(CategoryError.ImportError("JSON"))
                    }
                },
                failure = {
                    return Err(CategoryError.ImportError("JSON"))
                }
            )
        }
        return Ok(categories.map { it.toCategory() })
    }

}