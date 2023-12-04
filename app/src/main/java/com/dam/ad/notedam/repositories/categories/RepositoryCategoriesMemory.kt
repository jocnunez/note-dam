package com.dam.ad.notedam.repositories.categories

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.errors.CategoryError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.io.File
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class RepositoryCategoriesMemory: IRepositoryCategories{

    private val id = UUID.randomUUID()
    private val categories: MutableMap<UUID, Category> = mutableMapOf(
        id to Category(id, "Paisajes", "Sitios que visitar", 0u, listOf(
            com.dam.ad.notedam.models.Note.Text(UUID.randomUUID(), "Esto es una tarea de texto", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Image(UUID.randomUUID(), Uri.parse("https://img.freepik.com/fotos-premium/hermosos-paisajes-paisajes-naturales-hacen-que-personas-relajen-disfruten-fondo-pantalla_917506-214429.jpg"), false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Audio(UUID.randomUUID(), File("url de la nota de audio"), false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Sublist(UUID.randomUUID(), listOf(SublistItem(false, "Tarea 1"), SublistItem(false, "Tarea 2")), true, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Text(UUID.randomUUID(), "Esto es otra tarea de texto", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Image(UUID.randomUUID(), Uri.parse("https://www.hofmann.es/blog/wp-content/uploads/2021/09/HF_3_Paisajes_WEB-23.jpg"), false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Sublist(UUID.randomUUID(), listOf(), false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Text(UUID.randomUUID(), "Esto es la última tarea de texto", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Image(UUID.randomUUID(), Uri.parse("https://images.hola.com/imagenes/viajes/20200401164667/paisajes-de-espana-a-vista-de-pajaro-drone-aereas/0-806-353/paisajes-a-vista-de-pajaro-t.jpg"), false, LocalDateTime.now())))
    )

    override fun findByTitle(title: String): Result<Category, CategoryError> {
        val category = categories.values.find { it.title == title }
        return if (category != null) {
            Ok(category)
        } else {
            Err(CategoryError.CategoryNotFound())
        }
    }

    override fun findByPriority(priority: UInt): Result<Category, CategoryError> {
        val category = categories.values.find { it.priority == priority }
        return if (category != null) {
            Ok(category)
        } else {
            Err(CategoryError.CategoryNotFound())
        }
    }

    override fun getNotesSortedByPriority(): Iterable<Category> {
        return categories.values.sortedBy { it.priority }
    }

    override fun save(element: Category): Result<Category, CategoryError> {
        categories[element.uuid] = element
        return if(categories[element.uuid] == element){
            Ok(element)
        } else {
            Err(CategoryError.CategoryNotSaved())
        }
    }

    override fun saveAll(elements: Iterable<Category>) {
        elements.forEach { save(it) }
    }

    override fun deleteById(id: UUID): Result<Boolean, CategoryError> {
        val result = categories.remove(id) != null
        return if (result) {
            Ok(true)
        } else {
            Err(CategoryError.CategoryNotDeleted())
        }
    }

    override fun delete(element: Category): Result<Boolean, CategoryError> {
        return deleteById(element.uuid)
    }

    override fun deleteAll() {
        categories.clear()
    }

    override fun findAll(): Iterable<Category> {
        return categories.values
    }

    override fun findById(id: UUID): Result<Category, CategoryError> {
        val category = categories[id]
        return if (category != null) {
            Ok(category)
        } else {
            Err(CategoryError.CategoryNotFound())
        }
    }

    override fun existsById(id: UUID): Boolean {
        return categories.containsKey(id)
    }

    override fun count(): Int {
        return categories.size
    }
}