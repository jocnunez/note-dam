package com.dam.ad.notedam.services.storage

import android.content.Context
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.Todo
import java.time.LocalDate
import java.util.UUID

class CsvStorageService : IStorageService {
    private val categoriesFile = "categories.csv"
    private val todosFile = "todos.csv"
    private val sublistsFile = "sublists.csv"

    // TODO: validar los strings (no pueden contener | ni estar vacías)

    override fun write(context: Context, categories: List<Category>) {
        writeCategories(context, categories)
    }

    private fun writeCategories(context: Context, categories: List<Category>) {
        context.openFileOutput(categoriesFile, Context.MODE_PRIVATE).bufferedWriter()
            .use { writer ->
                writer.write(categories.joinToString("\n") {
                    "${it.uuid}|${it.name}|${it.createdAt}"
                })
            }
        writeTodos(context, categories)
    }

    private fun writeTodos(context: Context, categories: List<Category>) {
        context.openFileOutput(todosFile, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(categories.joinToString("\n") { cat ->
                categoryToCsvRows(context, cat)
            })
        }
    }

    private fun categoryToCsvRows(context: Context, category: Category): String {
        return category.todos.joinToString("\n") {
            "${category.uuid}|${it.type.name}|${it.title}|${it.createdAt}|${it.completed}|" +
                    when (it) {
                        is Todo.TextTodo -> it.text
                        is Todo.ImageTodo -> it.image
                        is Todo.AudioTodo -> it.audio
                        is Todo.SublistTodo -> {
                            writeSublist(context, it)
                            it.title
                        }
                    }
        }
    }

    private fun writeSublist(context: Context, sublistTodo: Todo.SublistTodo) {
        context.openFileOutput(sublistsFile, Context.MODE_APPEND).bufferedWriter().use { writer ->
            writer.append(
                sublistTodo.sublist.joinToString("\n") { item ->
                    "${sublistTodo.title}|${item.title}|${item.completed}"
                } + "\n"
            )
        }
    }

    override fun read(context: Context): List<Category> {
        if (!context.getFileStreamPath(categoriesFile).exists() ||
            !context.getFileStreamPath(todosFile).exists() ||
            !context.getFileStreamPath(sublistsFile).exists()
        ) return emptyList()

        return readCategories(context)
    }

    private fun readCategories(context: Context): List<Category> {
        return context.openFileInput(categoriesFile).bufferedReader().useLines { lines ->
            lines.map { line ->
                val (uuid, name, createdAt) = line.split("|")
                Category(
                    UUID.fromString(uuid),
                    name,
                    LocalDate.parse(createdAt),
                    readTodos(context, uuid)
                )
            }.toList()
        }
    }

    private fun readTodos(context: Context, catUuid: String): MutableList<Todo> {
        return context.openFileInput(todosFile).bufferedReader().useLines { lines ->
            lines.filter { it.startsWith(catUuid) }.map { line ->
                val (type, title, createdAt, completed, contents) = line.split("|").drop(1)
                when (type) {
                    "TEXT" -> Todo.TextTodo(
                        title,
                        LocalDate.parse(createdAt),
                        completed.toBoolean(),
                        contents
                    )

                    "IMAGE" -> Todo.ImageTodo(
                        title,
                        LocalDate.parse(createdAt),
                        completed.toBoolean(),
                        contents
                    )

                    "AUDIO" -> Todo.AudioTodo(
                        title,
                        LocalDate.parse(createdAt),
                        completed.toBoolean(),
                        contents
                    )

                    "SUBLIST" -> {
                        Todo.SublistTodo(
                            title,
                            LocalDate.parse(createdAt),
                            completed.toBoolean(),
                            readSublists(context, title)
                        )
                    }

                    else -> throw IllegalArgumentException("Unknown type: $type")
                }
            }
        }.toMutableList()
    }

    private fun readSublists(context: Context, todoTitle: String): MutableList<SublistItem> {
        return context.openFileInput(sublistsFile).bufferedReader().useLines { lines ->
            lines.filter { it.startsWith(todoTitle) }.map { line ->
                val (title, completed) = line.split("|").drop(1)
                SublistItem(title, completed.toBoolean())
            }.toMutableList()
        }
    }
}
