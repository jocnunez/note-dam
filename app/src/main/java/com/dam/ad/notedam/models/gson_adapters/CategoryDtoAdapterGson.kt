package com.dam.ad.notedam.models.gson_adapters

import com.dam.ad.notedam.models.dto.CategoryDto
import com.dam.ad.notedam.models.dto.NoteDto
import com.google.gson.*
import java.lang.reflect.Type

class CategoryDtoAdapterGson : JsonSerializer<CategoryDto>, JsonDeserializer<CategoryDto> {
    override fun serialize(src: CategoryDto, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("uuid", src.uuid)
        jsonObject.addProperty("title", src.title)
        jsonObject.addProperty("description", src.description)
        jsonObject.addProperty("priority", src.priority.toInt())

        val notesArray = JsonArray()
        src.notes.forEach { note ->
            notesArray.add(context.serialize(note))
        }
        jsonObject.add("notes", notesArray)

        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CategoryDto {
        val jsonObject = json.asJsonObject
        val uuid = jsonObject["uuid"].asString
        val title = jsonObject["title"].asString
        val description = jsonObject["description"].asString
        val priority = jsonObject["priority"].asInt.toUInt()

        val notes = if (jsonObject.has("notes")) {
            val notesArray = jsonObject.getAsJsonArray("notes")
            notesArray.map { context.deserialize<NoteDto>(it, NoteDto::class.java) }
        } else listOf()

        return CategoryDto(uuid, title, description, priority, notes)
    }
}
