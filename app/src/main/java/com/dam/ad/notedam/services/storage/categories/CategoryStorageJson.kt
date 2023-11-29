package com.dam.ad.notedam.services.storage.categories

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
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
    override fun saveAll(elements: Iterable<Category>, filePath: String): Result<Iterable<Category>, CategoryError> {
        val file = File(filePath + fileNameBase)
        return file.validate(FileAction.WRITE).mapBoth(
            success = {
                return try{
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val jsonString = gson.toJson(elements)
                    file.writeText(jsonString)
                    Ok(elements)
                }catch (e: Exception){
                    Err(CategoryError.ExportError("JSON"))
                }
            },
            failure = {
                Err(CategoryError.ExportError("JSON"))
            }
        )
    }

    override fun loadAll(filePath: String): Result<List<Category>, CategoryError> {
        val file = File(filePath + fileNameBase)
        return file.validate(FileAction.READ).mapBoth(
            success = {
                return try{
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val jsonString = file.readText()
                    val listType = object : TypeToken<List<Category>>() {}.type
                    val categories = gson.fromJson<List<Category>>(jsonString, listType)
                    Ok(categories)
                }catch (e: Exception){
                    Err(CategoryError.ImportError("JSON"))
                }
            },
            failure = {
                Err(CategoryError.ImportError("JSON"))
            }
        )
    }

}