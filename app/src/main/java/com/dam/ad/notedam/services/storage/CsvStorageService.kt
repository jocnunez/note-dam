package com.dam.ad.notedam.services.storage

import android.app.AlertDialog
import android.content.Context
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Todo
import java.io.FileNotFoundException
import java.io.IOException

class CsvStorageService : IStorageService {
    private val fileName = "categories.csv"

    override fun write(context: Context, content: List<Category>) {
        try {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter()
                .use { writer ->
                    writer.write(
                        content.joinToString(separator = "\n") { category ->
                            category.todos.joinToString(separator = "\n") { todo ->
                                "${category.name}#${todo.title}#${todo.content}"
                            }
                        }
                    )
                }
        } catch (e: FileNotFoundException) {
            AlertDialog.Builder(context).apply {
                setTitle("Error")
                setMessage("El fichero con los datos no existe")
                setPositiveButton("OK") { _, _ -> }
            }.show()
        } catch (e: IOException) {
            AlertDialog.Builder(context).apply {
                setTitle("Error")
                setMessage("Error al escribir el fichero con los datos")
                setPositiveButton("OK") { _, _ -> }
            }.show()
        }
    }

    override fun read(context: Context): List<Category> {
        val categories = mutableListOf<Category>()

        try {
            context.openFileInput(fileName).bufferedReader()
                .use { reader ->
                    reader.forEachLine { line ->
                        val (cat, name, content) = line.split("#")
                        val category = categories.find { it.name == cat }
                        category?.todos?.add(Todo(name, content))
                            ?: categories.add(Category(cat, mutableListOf(Todo(name, content))))
                    }
                }
        } catch (e: FileNotFoundException) {
            AlertDialog.Builder(context).apply {
                setTitle("Error")
                setMessage("El fichero con los datos no existe")
            }.show()
        } catch (e: IOException) {
            AlertDialog.Builder(context).apply {
                setTitle("Error")
                setMessage("Error al leer el fichero con los datos")
            }.show()
        }

        return categories
    }
}
