package com.dam.ad.notedam.services.storage.categories

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.dto.CategoriesXmlDto
import com.dam.ad.notedam.models.dto.CategoryDto
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.services.storage.StorageService
import com.dam.ad.notedam.utils.Utils
import com.dam.ad.notedam.utils.mappers.toCategory
import com.dam.ad.notedam.utils.mappers.toCategoryDto
import com.dam.ad.notedam.utils.validators.FileAction
import com.dam.ad.notedam.utils.validators.validate
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import org.simpleframework.xml.core.Persister
import java.io.File
import java.lang.Error

@RequiresApi(Build.VERSION_CODES.O)

class CategoryStorageXml: CategoryStorageService {
    private val fileNameBase = "_categories.xml"

    override fun export(element: Category, filePath: String, clearFiles: Boolean): Result<Category, CategoryError> {
        val fileUsing = filePath + element.uuid + fileNameBase
        if(clearFiles){
            Utils.clearFilesFn(listOf(fileUsing), filePath, fileNameBase, "XML")
        }
        val file = File(fileUsing)
        return file.validate(FileAction.WRITE).mapBoth(
            success = {
                try{
                    val persister = Persister()
                    persister.write(element.toCategoryDto(),file)
                    Ok(element)
                }catch (e: Exception){
                    Err(CategoryError.ExportError("XML"))
                }
            },
            failure = {
                Err(CategoryError.ExportError("XML"))
            }
        )
    }

    override fun exportAll(
        elements: Iterable<Category>,
        filePath: String,
        clearFiles: Boolean
    ): Result<Iterable<Category>, CategoryError> {
        val fileUsing = elements.map { filePath+it.uuid+fileNameBase }
        if(clearFiles){
            Utils.clearFilesFn(fileUsing, filePath, fileNameBase, "XML")
        }
        elements.forEach { category ->
        val file = File(filePath+category.uuid+fileNameBase)
            file.validate(FileAction.WRITE).mapBoth(
                success = {
                    try{
                        val persister = Persister()
                        persister.write(category.toCategoryDto(),file)
                    }catch (e: Exception){
                       return Err(CategoryError.ExportError("XML"))
                    }
                },
                failure = {
                  return  Err(CategoryError.ExportError("XML"))
                }
        )
        }
        return Ok(elements)
    }

    override fun loadAll(filePath: String): Result<List<Category>, CategoryError> {
        val files = File(filePath).listFiles{_,name->
            name.endsWith(fileNameBase)
        }
        if(files.isNullOrEmpty()){
            return  Err(CategoryError.ImportError("XML"))
        }
        val categories = mutableListOf<CategoryDto>()

        files.forEach { file->
            file.validate(FileAction.READ).mapBoth(
                success = {
                    try{
                        val persister = Persister()
                       categories.add(persister.read(CategoryDto::class.java,file))
                    }catch (e: Exception){
                        return Err(CategoryError.ImportError("XML"))
                    }
                },
                failure = {
                    return  Err(CategoryError.ImportError("XML"))
                }
            )
        }
        return Ok(categories.map { it.toCategory()})
    }
}