package com.dam.ad.notedam.services.storage

import android.content.Context
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Todo
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate

class JsonStorageService : IStorageService {
    private val file = "categories.json"
    private val gson = GsonBuilder()
        .registerTypeAdapter(Todo::class.java, TodoAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    override fun write(context: Context, categories: List<Category>) {
        val json = gson.toJson(categories)
        context.openFileOutput(file, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(json)
        }
    }

    override fun read(context: Context): List<Category> {
        if (!context.getFileStreamPath(file).exists()) return emptyList()

        val json = context.openFileInput(file).bufferedReader().readText()
        return gson.fromJson(json, Array<Category>::class.java).asList()
    }
}

class TodoAdapter : JsonSerializer<Todo>, JsonDeserializer<Todo> {
    override fun serialize(
        src: Todo?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        src?.let {
            jsonObject.addProperty("type", it.javaClass.name)
            jsonObject.add("data", context?.serialize(it))
        }
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Todo {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("type")?.asString
        val data = jsonObject?.get("data")
        return context?.deserialize(data, type?.let { Class.forName(it) }) as Todo
    }
}

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.toString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json?.asString)
    }
}