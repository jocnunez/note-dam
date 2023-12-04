package com.dam.ad.notedam.services.storage.categories

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.dto.CategoryDto
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.utils.Utils
import com.dam.ad.notedam.utils.mappers.toCategory
import com.dam.ad.notedam.utils.mappers.toCategoryDto
import com.dam.ad.notedam.utils.validators.FileAction
import com.dam.ad.notedam.utils.validators.validate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import java.io.File

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
                val xmlMapper = XmlMapper(
                    JacksonXmlModule().apply { setDefaultUseWrapper(false) }
                ).apply {
                    enable(SerializationFeature.INDENT_OUTPUT)
                    enable(SerializationFeature.WRAP_ROOT_VALUE)
                    enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                }

                try{
                    val dto = element.toCategoryDto()
                    xmlMapper.writeValue(file,dto)
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
        val xmlMapper = XmlMapper(
            JacksonXmlModule().apply { setDefaultUseWrapper(false) }
        ).apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            enable(SerializationFeature.WRAP_ROOT_VALUE)
            enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        }
        elements.forEach { category ->
            val file = File(filePath+category.uuid+fileNameBase)
            file.validate(FileAction.WRITE).mapBoth(
                success = {
                    try{
                        val dto = category.toCategoryDto()
                        xmlMapper.writeValue(file,dto)
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
        val xmlMapper = XmlMapper(
            JacksonXmlModule().apply { setDefaultUseWrapper(false) }
        ).apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            enable(SerializationFeature.WRAP_ROOT_VALUE)
            enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        }

        files.forEach { file->
            file.validate(FileAction.READ).mapBoth(
                success = {
                    try{
                        val dto = xmlMapper.readValue(file,CategoryDto::class.java)
                        categories.add(dto)
                    }catch (e: Exception){
                        return Err(CategoryError.ImportError("XML"))
                    }
                },
                failure = {
                    return  Err(CategoryError.ImportError("XML"))
                }
            )
        }
        val categoriesList = categories.map { it.toCategory() }
        return Ok(categoriesList)
    }
}