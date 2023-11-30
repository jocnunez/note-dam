package com.dam.ad.notedam.services.storage.categories

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.utils.Utils
import com.dam.ad.notedam.utils.validators.FileAction
import com.dam.ad.notedam.utils.validators.validate
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

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
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val jsonString = gson.toJson(element)
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
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val jsonString = gson.toJson(category)
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
        val categories = mutableListOf<Category>()
        files.forEach { file ->
            file.validate(FileAction.READ).mapBoth(
                success = {
                    try{
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val jsonString = file.readText()
                        val listType = object : TypeToken<List<Category>>() {}.type
                        categories.addAll(gson.fromJson(jsonString, listType))
                    }catch (e: Exception){
                        return Err(CategoryError.ImportError("JSON"))
                    }
                },
                failure = {
                    return Err(CategoryError.ImportError("JSON"))
                }
            )
        }
        return Ok(categories)
    }

}